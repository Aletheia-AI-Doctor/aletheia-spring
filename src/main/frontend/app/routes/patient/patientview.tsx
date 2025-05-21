import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import Loading from "~/components/Loading";
import {useGetPatientByIdQuery} from "~/features/patient/patientApiSlice";
import type {Route} from "./+types/patientview";
import {Link, useParams} from 'react-router';
import {faUserCircle} from '@fortawesome/free-solid-svg-icons';
import {useUpdatestatusMutation} from "~/features/patient/patientApiSlice";
import Button from '~/components/button';
import PatientDetailsCard from "~/components/patient-details-card";

export function meta() {
    return [
        {title: "Patient VIEW"},
        {name: "description", content: "Patient VIEW"},
    ];
}

export default function PatientViewPage() {
    const {id} = useParams();

    const {data: patient, isLoading, error, refetch} = useGetPatientByIdQuery(id!);

    if (isLoading || !patient) return <Loading message="Loading patient data..."/>;

    return (
        <div className="w-full p-6">
            {/* Patient Info Card */}
            <PatientDetailsCard patient={patient} refetch={refetch} />
            {patient.scans?.length > 0 ? (
                <div className="mt-8">
                    <h2 className="text-xl font-semibold text-gray-800 mb-4">Scans</h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                        {patient.scans.map((scan: any, index: number) => (
                            <div
                                key={index}
                                className="bg-white shadow-sm border rounded-lg overflow-hidden hover:shadow-lg transition cursor-pointer"
                            >
                                <div className="h-40 bg-gray-100 flex items-center justify-center overflow-hidden">
                                    {scan.image ? (
                                        <img src={scan.image} alt={`Scan ${index + 1}`}
                                             className="object-cover h-full w-full"/>
                                    ) : (
                                        <span className="text-gray-400 text-sm">No image</span>
                                    )}
                                </div>

                                <div className="p-4 text-sm text-gray-700">
                                    <p className="font-medium">Model Diagnosis:</p>
                                    <p className="text-gray-800 mb-2">{scan.modelDiagnosis?.name || "N/A"}</p>

                                    <p className="font-medium">Doctor Diagnosis:</p>
                                    <p className="text-gray-800">{scan.doctorDiagnosis?.name || "Not yet assigned"}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            ) : (
                <p className="text-gray-500 mt-6">No scans available for this patient.</p>
            )}


        </div>
    );
}