import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Loading from "~/components/Loading";
import { useGetPatientsQuery, useAddPatientMutation, useGetPatientByIdQuery } from "~/features/patient/patientApiSlice";
import type { Route } from "./+types/patientview";
import { Link, useParams } from 'react-router';
export function meta() {
    return [
        { title: "Patient VIEW" },
        { name: "description", content: "Patient VIEW" },
    ];
}
export default function ShowPatientPage() {
  const { id } = useParams();
  const { data: patients, isLoading,isError,error } = useGetPatientByIdQuery(id);
  const [patient] = useState();

  // if (isLoading || !patient) return <Loading message="Loading patient data..." />;
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
  const calculateAge = (birthday: string) => {
    const birthDate = new Date(birthday);
    const ageDifMs = Date.now() - birthDate.getTime();
    return Math.floor(ageDifMs / (1000 * 60 * 60 * 24 * 365));
  };

  return (
    <div className="p-6 max-w-6xl mx-auto flex gap-8">
      {/* Left side: Patient info */}
      <div className="w-1/3 border rounded p-4 shadow">
        {/* <div className="flex justify-center mb-4">
          <FontAwesomeIcon icon={faUserCircle} size="6x" className="text-gray-400" />
        </div> */}
        <div className="space-y-2">
          {/* <p><strong>Name:</strong> {patient.name}</p>
          <p><strong>Sex:</strong> {patient.sex}</p>
          <p><strong>Birthday:</strong> {patient.Birthday}</p>
          <p><strong>Age:</strong> {calculateAge(patient.Birthday)} years</p>
          <p><strong>Status:</strong> {patient.status}</p> */}
          {patient}
        </div>
      </div>

      {/* Right side: Scans grid */}
      {/* <div className="w-2/3">
        <h2 className="text-xl font-semibold mb-4">Scans</h2>
        <div className="grid grid-cols-3 gap-4">
          {scans.map((scan) => (
            <div
              key={scan.id}
              className="bg-white shadow-md border rounded-lg p-4 cursor-pointer hover:shadow-lg transition"
            >
              <div className="h-24 bg-gray-100 rounded flex items-center justify-center mb-2 text-gray-500">
                [Image]
              </div>
              <p className="font-medium text-sm mb-1">{scan.title}</p>
              <p className="text-xs text-gray-600">AI: {scan.aiDiagnosis}</p>
            </div>
          ))}
        </div>
      </div> */}
    </div>
  );
}
