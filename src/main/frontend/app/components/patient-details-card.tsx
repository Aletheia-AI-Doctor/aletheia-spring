import React from "react";
import {type Patient, useUpdatestatusMutation} from "~/features/patient/patientApiSlice";
import Card from "~/components/Card";
import StyledLink from "~/components/styled-link";

interface PatientDetailsCardProps {
    patient: Patient,
    refetch?: () => void;
}

export default function PatientDetailsCard({patient, refetch}: PatientDetailsCardProps) {

    const [updatePatientStatus] = useUpdatestatusMutation();

    const handleToggleStatus = async (newStatus: "PENDING" | "DIAGNOSED") => {
        if (patient.status === newStatus) return; // do nothing if already selected

        try {
            await updatePatientStatus({id: patient.id, status: newStatus});
            refetch && refetch();
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
    <Card className="flex flex-col md:flex-row items-center gap-6 mb-6">

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
                <p className="text-gray-500 font-medium">Dignose</p>
                <StyledLink to={`/diagnose?patientId=${patient.id}`}>
                    Add scan
                </StyledLink>

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
    </Card>
);
}