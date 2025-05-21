import React from 'react';
import Loading from "~/components/Loading";
import {useGetPatientByIdQuery} from "~/features/patient/patientApiSlice";
import {useParams} from 'react-router';
import PatientDetailsCard from "~/components/patient-details-card";
import ScansTable from "~/components/ScansTable";
import Card from "~/components/Card";

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
        <div className="w-full">
            {/* Patient Info Card */}
            <PatientDetailsCard patient={patient} refetch={refetch} />

            <Card>
                <ScansTable patientId={patient.id} />
            </Card>
        </div>
    );
}