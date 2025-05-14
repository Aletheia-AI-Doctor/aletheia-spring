import React, { useState } from 'react';
import { FilePond, registerPlugin } from 'react-filepond';
import 'filepond/dist/filepond.min.css';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLeftLong } from '@fortawesome/free-solid-svg-icons';
import {
    useUploadScanMutation,
    useGetModelsQuery,
    useSaveScanMutation,
    useGetScansQuery
} from "~/features/scans/scansApiSlice";
import type { Route } from "./+types/diagnose";
import {Link, useParams} from "react-router";
import Loading from "~/components/Loading";
import Button from "~/components/button";
import Modal from "~/components/modal";
import PatientForm from "~/components/patient-form";
import type {Patient} from "~/features/patient/patientApiSlice";
import If from "~/components/if";

// Register the plugins
registerPlugin(FilePondPluginFileValidateType, FilePondPluginImagePreview);

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Diagnose" },
        { name: "description", content: "Welcome to React Router!" },
    ];
}

export default function DiagnosisPage() {
    const {model: selectedModel} = useParams();

    const [file, setFile] = useState<File | null>(null);
    const [diagnosisResult, setDiagnosisResult] = useState<string | null>(null);
    const [imagePath, setImagePath] = useState<string | null>(null);

    // API hooks
    const { data: models = [], isLoading: isLoadingModels } = useGetModelsQuery();
    const { data, isLoading: isLoadingScans, refetch } = useGetScansQuery();

    const [uploadScan, {isLoading: isDiagnosing}] = useUploadScanMutation();
    const [saveScan, {isLoading: isSavingScan}] = useSaveScanMutation();
    const [saveMessage, setSaveMessage] = useState<string | null>(null);
    const [open, setOpen] = useState<boolean>(false);

    async function handleSaveGuest() {
        const response = await saveScan({
            modelDiagnosis: diagnosisResult!,
            imagePath: imagePath!,
            model: selectedModel!,
        });

        if(response.error) {
            // @ts-ignore
            setSaveMessage(response.error.message ?? "Failed to save scan");
            return;
        }

        setSaveMessage("Scan saved successfully");
        handleSave();
    }

    async function handleSavePatient(patient: Patient) {
        const response = await saveScan({
            modelDiagnosis: diagnosisResult!,
            imagePath: imagePath!,
            model: selectedModel!,
            patientId: patient.id,
        });

        if(response.error) {
            // @ts-ignore
            setSaveMessage(response.error.message ?? "Failed to save scan");
            return;
        }

        setSaveMessage("Scan saved successfully");
        handleSave();
    }

    function handleSave() {
        setFile(null);
        setDiagnosisResult(null);
        refetch();
    }

    const handleFileUpload = (fileItems: any[]) => {
        if (fileItems.length > 0) {
            setFile(fileItems[0].file);
            setDiagnosisResult(null);
            setImagePath(null);
        } else {
            setFile(null);
        }
    };

    const handleDiagnose = async () => {
        if (!file || !selectedModel) return;
        try {
            const result = await uploadScan({
                scan: file,
                model: selectedModel.toLowerCase()
            }).unwrap();
            setDiagnosisResult(result.name);
            setImagePath(result.imagePath);
        } catch (error) {
            setDiagnosisResult("Diagnosis failed. Please try again.");
        }
    };

    return (
        <>
        <div className="p-4 max-w-4xl mx-auto">
            <h1 className="text-2xl font-bold mb-4">Medical Scan Diagnosis</h1>

            {!selectedModel ? (
                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Select Scan Type</h2>
                    {isLoadingModels && <Loading message="Loading available models" />}

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {models.map((model) => (
                            <Link
                                key={model.id}
                                to={`/diagnose/${model.slug}`}
                                className="p-6 bg-blue-50 hover:bg-blue-100 rounded-lg text-center transition-colors"
                            >
                                <h3 className="font-medium text-blue-800">{model.name}</h3>
                            </Link>
                        ))}
                    </div>
                </div>
            ) : (
                <div className="bg-white rounded-lg shadow overflow-hidden">
                    <div className="bg-gray-50 p-4 flex items-center border-b">
                        <Link
                            to={`/diagnose`}
                            className="p-2 text-gray-600 hover:text-gray-900 mr-4"
                        >
                            <FontAwesomeIcon icon={faLeftLong} size="lg"/>
                        </Link>
                        <h2 className="text-xl font-semibold">
                            {models.find(m => m.slug === selectedModel)?.name || selectedModel}
                        </h2>
                    </div>

                    {/* File upload section */}
                    <div className="p-6">
                        <div className="mb-6">
                            <h3 className="text-lg font-medium mb-3">Upload Your Scan</h3>
                            <FilePond
                                allowMultiple={false}
                                acceptedFileTypes={['image/*']}
                                onupdatefiles={handleFileUpload}
                                credits={false}
                                files={file ? [file] : []}
                                labelIdle='Drag & Drop your scan or <span class="filepond--label-action">Browse</span>'
                            />
                        </div>

                        {/* Diagnosis button and results */}
                        <div className="mt-6">
                            <div className="flex items-center">
                                <Button
                                    width="w-auto"
                                    color="light-blue"
                                    onClick={handleDiagnose}
                                    disabled={!file || isDiagnosing}
                                >
                                    {isDiagnosing ? (
                                        <div className="flex space-x-2 items-center">
                                            <Loading size="size-6" color="text-gray-800"/>
                                            <span className="text-gray-800">Running Diagnosis</span>
                                        </div>
                                    ) : 'Run Diagnosis'}
                                </Button>
                            </div>

                            {diagnosisResult && (
                                <div>
                                    <div className="mt-6 mb-3 p-4 bg-blue-50 rounded-md">
                                        <h3 className="font-medium text-blue-800 mb-2">Diagnosis Result</h3>
                                        <p className="text-gray-800">{diagnosisResult}</p>
                                    </div>
                                    <If
                                        replacement={<Loading message={"Saving scan..."}/>}
                                        condition={!isSavingScan}>
                                        <div className="mt-6 flex items-center space-x-6 justify-center">
                                            <Button disabled={isSavingScan} onClick={handleSaveGuest} value="guest"
                                                    color="gray">Save as guest</Button>
                                            <Button disabled={isSavingScan} onClick={() => setOpen(true)}>Save to
                                                patient</Button>
                                        </div>
                                    </If>
                                    {saveMessage && (
                                        <div className="mt-4 p-4 bg-green-50 rounded-md">
                                            <p className="text-green-800">{saveMessage}</p>
                                        </div>
                                    )}

                                    <Modal onClose={() => setOpen(false)} open={open}>
                                        <PatientForm onSuccess={handleSavePatient} onClose={() => setOpen(false)}/>
                                    </Modal>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>

            <If
                replacement={<Loading message="Loading scans..." />}
                condition={!isLoadingScans}>
                <div className="">
                    {data && data.scans.map(scan => (
                        <div key={scan.id} className="p-4 bg-gray-50 rounded-md mb-4">
                            {scan.modelDiagnosis.name}
                        </div>
                    ))}
                </div>
            </If>
        </>
    );
}