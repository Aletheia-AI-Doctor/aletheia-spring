import React, {useEffect, useState} from 'react';
import { FilePond, registerPlugin } from 'react-filepond';
import 'filepond/dist/filepond.min.css';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLeftLong } from '@fortawesome/free-solid-svg-icons';
import {ROOT_URL} from "app/base/consts";
import {useUploadScanMutation} from "~/features/scans/scansApiSlice";

// Register the plugins
registerPlugin(FilePondPluginFileValidateType, FilePondPluginImagePreview);

export default function DiagnosisPage() {
    const [selectedType, setSelectedType] = useState('');
    const [fileUploaded, setFileUploaded] = useState(false);
    const [file, setFile] = useState(null);

    const [uploadScan, {isLoading: isUploading, isSuccess, data}] = useUploadScanMutation();

    useEffect(() => {
        if (!isUploading && isSuccess) {
            console.log("Diagnosis result: ", data.name);
        }
    }, [isSuccess, isUploading]);

    const handleTypeSelect = (typeName : string) => {
        setSelectedType(typeName);
    };

    const handleFileUpload = (fileItems : any[]) => {
        if (fileItems.length > 0) {
            setFileUploaded(true);
            setFile(fileItems[0].file);
        } else {
            setFileUploaded(false);
            setFile(null);
        }

        // Upload the file when it's selected
        if (fileItems.length > 0) {
            uploadScan({scan: fileItems[0].file, model: selectedType.toLowerCase()});
        }
    };

    const handleReturn = () => {
        setSelectedType('');
        setFileUploaded(false);
        setFile(null);
    };

    return (
        <div className="p-4">
            <h1 className="text-xl font-semibold">Diagnose</h1>
            <p>Diagnose image </p>
            {ROOT_URL}
            <p>chose a scan type, attach file, and diagnose!</p>
            {!selectedType ? (
                <div className="mt-10 p-10 border rounded">
                    <h2 className="text-lg font-semibold">Select Scan Type</h2>
                    <div className="flex space-x-4 mt-2 justify-center">
                        <button
                            className="px-10 py-10 bg-blue-500 text-white rounded"
                            onClick={() => handleTypeSelect('MRI')}
                        >
                            MRI
                        </button>
                        <button
                            className="px-10 py-10 bg-green-500 text-white rounded"
                            onClick={() => handleTypeSelect('X-Ray')}
                        >
                            X-Ray
                        </button>
                        <button
                            className="px-10 py-10 bg-purple-500 text-white rounded"
                            onClick={() => handleTypeSelect('Memograph')}
                        >
                            Memograph
                        </button>
                    </div>
                </div>
            ) : (
                <div className="mt-4">
                    <div className="p-2 flex items-center justify-start">
                        <button
                            className="p-5 text-gray-1000 hover:text-gray-800"
                            onClick={handleReturn}
                        >
                            <FontAwesomeIcon icon={faLeftLong} size="lg" />
                        </button>
                        <h2 className="text-lg font-semibold">Type: {selectedType}</h2>
                    </div>

                    <div className="mt-4 p-4 border rounded">
                        <h2 className="text-lg font-semibold">Attach Scan</h2>
                        <FilePond
                            allowMultiple={false}
                            acceptedFileTypes={['image/*']}
                            onupdatefiles={handleFileUpload}
                            labelIdle='Drag & Drop your scan or <span class="filepond--label-action">Browse</span>'
                        />
                    </div>

                    <div className="mt-4">
                        <button
                            className={`px-4 py-2 bg-blue-500 text-white rounded ${
                                !fileUploaded ? 'opacity-50 cursor-not-allowed' : ''
                            }`}
                            disabled={!fileUploaded}
                        >
                            Diagnose
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}