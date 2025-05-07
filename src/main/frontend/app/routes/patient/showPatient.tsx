import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Loading from "~/components/Loading";
import { useGetPatientsQuery, useAddPatientMutation } from "~/features/patient/patientApiSlice";
import type { Route } from "./+types/showPatient";
import { Link } from 'react-router';
export function meta() {
    return [
        { title: "Patient Management" },
        { name: "description", content: "Manage patient records" },
    ];
}

export default function PatientPage() {
    const { 
        data: patients = [], 
        isLoading, 
        isError, 
        error 
    } = useGetPatientsQuery();
    const [addPatient, { error: addError }] = useAddPatientMutation();
    
    const [showAddForm, setShowAddForm] = useState(false);
    const [newPatient, setNewPatient] = useState({
        name: '',
        sex: '',
        admissionDate: '',
        status: 'pending'
    });

    const handleAddPatient = async () => {
        try {
            await addPatient(newPatient).unwrap();
            setShowAddForm(false);
            setNewPatient({
                name: '',
                sex: '',
                admissionDate: '',
                status: 'pending'
            });
        } catch (err) {
            console.error('Failed to add patient:', err);
        }
    };

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
        );
    }

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

    return (
        <div className="container mx-auto p-4 max-w-6xl">
            <h1 className="text-2xl font-bold mb-6">Patients</h1>
            
            {/* Error message for add patient */}
            {addError && (
                <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-4">
                    <p>Failed to add patient: {(addError as any)?.data?.message || 'Unknown error'}</p>
                </div>
            )}

            <div className="flex justify-between items-center mb-6">
                <button 
                    onClick={() => setShowAddForm(true)}
                    className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded"
                >
                    Add new patient
                </button>
            </div>

            {showAddForm && (
                <div className="mb-6 p-4 border rounded-lg shadow-md bg-white">
                    <h2 className="text-lg font-semibold mb-4">Add New Patient</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Name*</label>
                            <input
                                type="text"
                                value={newPatient.name}
                                onChange={(e) => setNewPatient({...newPatient, name: e.target.value})}
                                className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Admission Date*</label>
                            <input
                                type="date"
                                value={newPatient.admissionDate}
                                onChange={(e) => setNewPatient({...newPatient, admissionDate: e.target.value})}
                                className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Sex*</label>
                            <select
                                value={newPatient.sex}
                                onChange={(e) => setNewPatient({...newPatient, sex: e.target.value})}
                                className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                                required
                            >
                                <option value="">Select</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>
                            </select>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Status*</label>
                            <select
                                value={newPatient.status}
                                onChange={(e) => setNewPatient({...newPatient, status: e.target.value})}
                                className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                                required
                            >
                                <option value="pending">Pending</option>
                                <option value="discharged">Diagnosed</option>
                            </select>
                        </div>
                    </div>
                    <div className="flex justify-end space-x-3">
                        <button 
                            onClick={() => setShowAddForm(false)}
                            className="bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium py-2 px-4 rounded"
                        >
                            Cancel
                        </button>
                        <button 
                            onClick={handleAddPatient}
                            disabled={!newPatient.name || !newPatient.admissionDate || !newPatient.sex}
                            className="bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-4 rounded disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            Save Patient
                        </button>
                    </div>
                </div>
            )}

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Admission Date</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">State</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {patients.length > 0 ? (
                            patients.map((patient) => (
                                <tr key={patient.id} className="hover:bg-gray-50">
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="text-sm font-medium text-gray-900">{patient.name}</div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="text-sm text-gray-500">
                                            {new Date(patient.admissionDate).toLocaleDateString('en-US', {
                                                year: 'numeric',
                                                month: '2-digit',
                                                day: '2-digit'
                                            })}
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                            patient.status === 'pending' ? 'bg-yellow-100 text-yellow-800' :
                                            patient.status === 'active' ? 'bg-green-100 text-green-800' :
                                            'bg-gray-100 text-gray-800'
                                        }`}>
                                            {patient.status}
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                        <Link 
                                            to={`/patients/${patient.id}`} 
                                            className="text-blue-600 hover:text-blue-900 mr-4"
                                        >
                                            View
                                        </Link>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={4} className="px-6 py-4 text-center text-sm text-gray-500">
                                    No patients found
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}