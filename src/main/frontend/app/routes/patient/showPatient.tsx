import React, {useState} from 'react';
import {useGetPatientsQuery} from "~/features/patient/patientApiSlice";
import {Link} from 'react-router';
import PatientForm from "~/components/patient-form";
import Loading from "~/components/Loading";
import Modal from "~/components/modal";
import Button from "~/components/button";

export function meta() {
    return [
        {title: "Patient Management"},
        {name: "description", content: "Manage patient records"},
    ];
}

export default function PatientPage() {
    const {
        data: patients,
        isLoading,
    } = useGetPatientsQuery();

    const [showAddForm, setShowAddForm] = useState(false);

    if (isLoading || !patients) {
        return <Loading/>;
    }

    return (
        <div className="container mx-auto p-4 max-w-6xl">
            <h1 className="text-2xl font-bold mb-6">Patients</h1>

            <div className="flex justify-between items-center mb-6">
                <Button
                    width="w-auto"
                    onClick={() => setShowAddForm(true)}
                >
                    Add patient
                </Button>
            </div>

            <Modal open={showAddForm} onClose={() => setShowAddForm(false)}>
                <PatientForm onClose={() => setShowAddForm(false)}/>
            </Modal>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Admission
                            Date
                        </th>
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
                                        {patient.admissionDate.toString()}
                                    </div>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                        <span
                                            className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                                patient.status === 'pending' ? 'bg-yellow-100 text-yellow-800' :
                                                    patient.status === 'discharged' ? 'bg-green-100 text-green-800' :
                                                        'bg-gray-100 text-gray-800'
                                            }`}>
                                            {patient.status}
                                        </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                    <Link
                                        to={`/patient/${patient.id}`}
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