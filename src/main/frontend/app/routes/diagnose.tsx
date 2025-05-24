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
    useGetScansQuery, type Diagnosis
} from "~/features/scans/scansApiSlice";
import type { Route } from "./+types/diagnose";
import {Link, useParams, useSearchParams} from "react-router";
import Loading from "~/components/Loading";
import Button from "~/components/button";
import Modal from "~/components/modal";
import PatientForm from "~/components/patient-form";
import {type Patient, useGetPatientByIdQuery} from "~/features/patient/patientApiSlice";
import If from "~/components/if";
import ScansTable from "~/components/ScansTable";
import Card from "~/components/Card";
import PatientDetailsCard from "~/components/patient-details-card";
import {ROOT_URL} from "~/base/consts";

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
    const [params, setParams] = useSearchParams();
    const patientId = params.get('patientId');
    const [refetchNow, setRefetchNow] = useState(false);

    const {data: patient, isLoading: isPatientLoading} = useGetPatientByIdQuery(patientId!, {
        skip: !patientId,
    });

    const [file, setFile] = useState<File | null>(null);
    const [diagnosisResult, setDiagnosisResult] = useState<Diagnosis | null>(null);

    // API hooks
    const { data: models = [], isLoading: isLoadingModels } = useGetModelsQuery();

    const [uploadScan, {isLoading: isDiagnosing}] = useUploadScanMutation();
    const [saveScan, {isLoading: isSavingScan}] = useSaveScanMutation();
    const [saveMessage, setSaveMessage] = useState<string | null>(null);
    const [open, setOpen] = useState<boolean>(false);

    async function handleSavePatient(patient: Patient) {
        const response = await saveScan({
            modelDiagnosis: diagnosisResult!.name,
            imageResponsePath: diagnosisResult!.imageResponsePath,
            imagePath: diagnosisResult!.imagePath,
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
        setRefetchNow(r => !r);
    }

    const handleFileUpload = (fileItems: any[]) => {
        if (fileItems.length > 0) {
            setFile(fileItems[0].file);
            setDiagnosisResult(null);
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
            setDiagnosisResult(result);
        } catch (error) {
            setDiagnosisResult(null);
        }
    };

    return (
        <>
        <div className="p-4 max-w-4xl mx-auto">

            {!isPatientLoading && patient && (
                <PatientDetailsCard patient={patient} />
            )}

            <h1 className="text-2xl font-bold mb-4">Medical Scan Diagnosis</h1>

            {!selectedModel ? (
                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Select Scan Type</h2>
                    {isLoadingModels && <Loading message="Loading available models" />}

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {models.map((model) => (
                            <Link
                                key={model.id}
                                to={patientId ? `/diagnose/${model.slug}?patientId=${patientId}` : `/diagnose/${model.slug}`}
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
                            to={patientId ? `/diagnose?patientId=${patientId}` : `/diagnose`}
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
                                        <p className="text-gray-800">{diagnosisResult.name}</p>
                                    </div>
                                    <If
                                        replacement={<Loading message={"Saving scan..."}/>}
                                        condition={!isSavingScan}>
                                        <div className="mt-6 flex items-center space-x-6 justify-center">
                                            <Button disabled={isSavingScan} onClick={handleSave}
                                                    color="gray">Cancel</Button>
                                            {patient ?
                                                (
                                                    <Button disabled={isSavingScan}
                                                            onClick={() => handleSavePatient(patient)}>
                                                        Save to patient
                                                    </Button>
                                                )
                                                    : (
                                                <Button disabled={isSavingScan} onClick={() => setOpen(true)}>Save to
                                                    patient</Button>
                                                )}
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

                            {diagnosisResult?.imageResponsePath && (
                                <div className="mt-4">
                                    <h3 className="font-medium text-blue-800 mb-2">Annotated Scan</h3>
                                    <img className="rounded w-64" src={diagnosisResult.imageResponseUrl} alt="Prediction result" />
                                </div>
                                )}
                        </div>
                    </div>
                </div>
            )}
        </div>

            <Card>
                <ScansTable refetchNow={refetchNow} patientId={patientId ?? undefined} />
            </Card>
        </>
    );
}