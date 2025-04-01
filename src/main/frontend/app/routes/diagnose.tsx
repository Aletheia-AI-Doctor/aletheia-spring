import React, { useState } from 'react';
import { FilePond, registerPlugin } from 'react-filepond';
import 'filepond/dist/filepond.min.css';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLeftLong } from '@fortawesome/free-solid-svg-icons';
import { useUploadScanMutation, useGetModelsQuery } from "~/features/scans/scansApiSlice";

// Register the plugins
registerPlugin(FilePondPluginFileValidateType, FilePondPluginImagePreview);

export default function DiagnosisPage() {
    // State management
    const [selectedModel, setSelectedModel] = useState<string | null>(null);
    const [file, setFile] = useState<File | null>(null);
    const [isDiagnosing, setIsDiagnosing] = useState(false);
    const [diagnosisResult, setDiagnosisResult] = useState<string | null>(null);

    // API hooks
    const { data: models = [], isLoading: isLoadingModels } = useGetModelsQuery();
    const [uploadScan] = useUploadScanMutation();

    // Event handlers
    const handleModelSelect = (modelSlug: string) => {
        setSelectedModel(modelSlug);
        setFile(null);
        setDiagnosisResult(null);
    };

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

        setIsDiagnosing(true);
        try {
            const result = await uploadScan({
                scan: file,
                model: selectedModel.toLowerCase()
            }).unwrap();
            setDiagnosisResult(result.name);
        } catch (error) {
            console.error("Diagnosis failed:", error);
            setDiagnosisResult("Diagnosis failed. Please try again.");
        } finally {
            setIsDiagnosing(false);
        }
    };

    const handleReset = () => {
        setSelectedModel(null);
        setFile(null);
        setDiagnosisResult(null);
    };

    return (
        <div className="p-4 max-w-4xl mx-auto">
            <h1 className="text-2xl font-bold mb-4">Medical Scan Diagnosis</h1>

            {!selectedModel ? (
                // Model selection view
                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Select Scan Type</h2>
                    {isLoadingModels ? (
                        <p>Loading available models...</p>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                            {models.map((model) => (
                                <button
                                    key={model.id}
                                    className="p-6 bg-blue-50 hover:bg-blue-100 rounded-lg transition-colors"
                                    onClick={() => handleModelSelect(model.slug)}
                                >
                                    <h3 className="font-medium text-blue-800">{model.name}</h3>
                                    <p className="text-sm text-gray-600 mt-1">{model.description || ''}</p>
                                </button>
                            ))}
                        </div>
                    )}
                </div>
            ) : (
                // Diagnosis workflow view
                <div className="bg-white rounded-lg shadow overflow-hidden">
                    {/* Header with back button */}
                    <div className="bg-gray-50 p-4 flex items-center border-b">
                        <button
                            onClick={handleReset}
                            className="p-2 text-gray-600 hover:text-gray-900 mr-4"
                        >
                            <FontAwesomeIcon icon={faLeftLong} size="lg" />
                        </button>
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
                                labelIdle='Drag & Drop your scan or <span class="filepond--label-action">Browse</span>'
                            />
                        </div>

                        {/* Diagnosis button and results */}
                        <div className="mt-6">
                            <button
                                onClick={handleDiagnose}
                                disabled={!file || isDiagnosing}
                                className={`px-6 py-3 rounded-md font-medium ${
                                    !file || isDiagnosing
                                        ? 'bg-gray-300 cursor-not-allowed'
                                        : 'bg-blue-600 hover:bg-blue-700 text-white'
                                }`}
                            >
                                {isDiagnosing ? 'Processing...' : 'Run Diagnosis'}
                            </button>

                            {diagnosisResult && (
                                <div className="mt-6 p-4 bg-blue-50 rounded-md">
                                    <h3 className="font-medium text-blue-800 mb-2">Diagnosis Result</h3>
                                    <p className="text-gray-800">{diagnosisResult}</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}