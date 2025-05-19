import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import Loading from "~/components/Loading";
import { useGetPatientByIdQuery} from "~/features/patient/patientApiSlice";
import type {Route} from "./+types/patientview";
import {Link, useParams} from 'react-router';
import {faUserCircle} from '@fortawesome/free-solid-svg-icons';
import {useUpdatestatusMutation} from "~/features/patient/patientApiSlice";
import Button from '~/components/button';

export function meta() {
    return [
        {title: "Patient VIEW"},
        {name: "description", content: "Patient VIEW"},
    ];
}

export default function PatientViewPage() {
    const {id} = useParams();
    const {data: patient, isLoading, isError, error,refetch} = useGetPatientByIdQuery(id!);
    console.log(patient?.status)
    const [updatePatientStatus] = useUpdatestatusMutation();
    
    if (isLoading || !patient) return <Loading message="Loading patient data..."/>;
    
    if (isError) {
        console.error('Error fetching patients:', error);
        return (
            <div className="flex flex-col items-center justify-center h-64 p-4">
                <p className="text-red-500 font-bold mb-2">Error loading patients</p>
                <p className="text-red-500 text-sm">{(error as any)?.data?.message || 'Unknown error occurred'}</p>
                <button
                    onClick={() => window.location.reload()}
                    className="mt-4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                >
                    Retry
                </button>
            </div>
        );
    }
const handleToggleStatus = async (newStatus: "PENDING" | "DIAGNOSED") => {
  if (patient.status === newStatus) return; // do nothing if already selected

  try {
    await updatePatientStatus({ id: patient.id, status: newStatus });
    await refetch(); // update UI
  } catch (error) {
    console.error("Failed to update status", error);
  }
};
    const calculateAge = (birthday: string) => {
        if (!birthday) return null;
        const birthDate = new Date(birthday);
        if (isNaN(birthDate.getTime())) return null;
        const ageDifMs = Date.now() - birthDate.getTime();
        return Math.floor(ageDifMs / (1000 * 60 * 60 * 24 * 365));
    };

    return (
        <div className="w-full p-6">
            {/* Patient Info Card */}
            <div className="w-full bg-white rounded-xl shadow-md p-6 flex flex-col md:flex-row items-center gap-6 mb-6">
                {/* Avatar */}
                <div
                    className="w-28 h-28 flex-shrink-0 rounded-full bg-gray-200 flex items-center justify-center shadow-inner">
                    <FontAwesomeIcon icon={faUserCircle} size="3x" className="text-gray-400"/>
                </div>

                {/* Info */}
                <div className="flex-1 grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm text-gray-800">
                    <div>
                        <p className="text-gray-500 font-medium">Name</p>
                        <p>{patient.name}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 font-medium">Sex</p>
                        <p>{patient.sex}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 font-medium">Birthdate</p>
                        <p>{patient.birthdate || "N/A"}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 font-medium">Age</p>
                        <p>{calculateAge(patient.birthdate) ?? "Unknown"} years</p>
                    </div>
                    <div>
                      <p className="text-gray-500 font-medium">dignose</p>
                      <Button color='primary' padding="px-15 py-3" width='40px'  onClick={() => window.location.href = `/diagnose`}>
                        add scan
                      </Button>
   
                    </div>



                    <div>
<p className="text-gray-500 font-medium mb-2">Status</p>
<div className="flex items-center">
  <div className="flex bg-white border border-gray-300 rounded-full overflow-hidden">
    <button
      onClick={() => handleToggleStatus("PENDING")}
      className={`px-4 py-1 text-sm font-semibold transition ${
        patient.status === "PENDING"
          ? "bg-yellow-400 text-black"
          : "text-gray-700 hover:bg-gray-100"
      }`}
    >
      Pending
    </button>
    <button
      onClick={() => handleToggleStatus("DIAGNOSED")}
      className={`px-4 py-1 text-sm font-semibold transition ${
        patient.status === "DIAGNOSED"
          ? "bg-green-500 text-white"
          : "text-gray-700 hover:bg-gray-100"
      }`}
    >
      Diagnosed
    </button>
  </div>
</div>
                    </div>
                </div>
            </div>
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
              <img src={scan.image} alt={`Scan ${index + 1}`} className="object-cover h-full w-full" />
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