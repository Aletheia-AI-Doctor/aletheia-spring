import React from "react";
import { useNavigate } from "react-router";
import type { Route } from "./+types/home";
import{useGetDoctorPatientsQuery} from "~/features/doctor/doctorDashboardApiSlice";
import { useGetDoctorAttributesQuery, useGetDoctorActivityLogQuery } from "~/features/doctor/doctorApiSlice";
import Loading from "~/components/Loading";


export function meta({}: Route.MetaArgs) {
    return [
        { title: "Dashboard" },
        { name: "description", content: "Welcome to React Router!" },
    ];
}

export default function Home(){
    const navigate = useNavigate();

    const {
        data: doctor,
        isLoading: isLoadingDoctor,
    } = useGetDoctorAttributesQuery();

    const{
        data: patientsData,
        isLoading: isLoadingPatients,
    } = useGetDoctorPatientsQuery();

    const{
        data: activityLog,
        isLoading: isActivityLoading,
    } = useGetDoctorActivityLogQuery();

    if (isLoadingDoctor || isLoadingPatients || isActivityLoading || !activityLog || !doctor || !patientsData) {
        return <Loading />;
    }

    return(
        <div className="max-w-4xl mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
            <div className="flex items-center space-x-4 mb-6">
                <h1 className="text-4xl font-bold text-blue-600">
                    Hello! {doctor?.name ?? "Doctor"}
                    <span className="ml-2 text-blue-400 text-lg">MD</span>
                </h1>
            </div>
            <div className="mb-4">
                <div className="border border-gray-200 bg-gray-50 rounded-lg p-4">
                    <p className="text-gray-700 mb-2">
                        <strong>Number of Patients:</strong> {patientsData?.total}
                    </p>
                    <p className="text-gray-700 mb-2">
                        <strong>Diagnosed Patients:</strong> {patientsData?.diagnosed ?? 0}
                    </p>
                    <p className="text-gray-700 mb-2">
                        <strong>Pending Patients:</strong> {patientsData?.pending ?? 0}
                    </p>
                </div>

            </div>
            <div className="mb-4">
                <h2 className="text-2xl font-semibold text-gray-700 mb-2">Activity Log:</h2>
                <div className="border border-gray-200 bg-gray-50 rounded-lg p-4">
                    {activityLog.length > 0 ? (
                        <ul className="list-disc pl-5 text-gray-700">
                            {activityLog.map((activity, index) => (
                                <li key={index}>{activity.action}</li>
                            ))}
                        </ul>
                    ) : (
                        <p className="text-gray-500 italic">\\ no recent activity</p>
                    )}
                </div>
            </div>
        </div>
    );
}