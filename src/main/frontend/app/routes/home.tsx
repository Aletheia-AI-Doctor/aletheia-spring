import React from "react";
import {Link} from "react-router";
import type { Route } from "./+types/home";
import{useGetDoctorPatientsQuery} from "~/features/doctor/doctorDashboardApiSlice";
import {useGetRecentActivityLogsQuery} from "~/features/doctor/doctorActivityApiSlice";
import Loading from "~/components/Loading";
import {useAppSelector} from "~/base/hooks";
import Title from "~/components/title";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Dashboard" },
        { name: "description", content: "Welcome to Aletheia" },
    ];
}

export default function Home(){
    const doctor = useAppSelector((state) => state.auth.doctor);

    const{
        data: patientsData,
        isLoading: isLoadingPatients,
    } = useGetDoctorPatientsQuery();

    const{
        data: activityLog,
        isLoading: isActivityLoading,
    } = useGetRecentActivityLogsQuery();

    if (isLoadingPatients || isActivityLoading || !activityLog || !patientsData || !doctor) {
        return <Loading />;
    }

    return(
        <div className="max-w-4xl mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
            <div className="flex items-center space-x-4 mb-6">
                <Title>Hello, {doctor.name}</Title>
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
                            <div>
                                <ul className="list-disc pl-5 text-gray-700 space-y-2">
                                    {activityLog.map((log) => (
                                        <li key={log.id}>
                                            <span className="font-semibold">{log.action}</span> – {log.description}
                                            <div className="text-sm text-gray-500">
                                                {new Date(log.createdAt).toLocaleString()}
                                            </div>
                                        </li>
                                    ))}
                                </ul>

                                <Link to="/activity" className="text-blue-600 hover:underline mt-4 block">
                                    View all activity -{">"}
                                </Link>
                            </div>
                        ) : (
                            <p className="text-gray-500 italic">No recent activity</p>
                        )}
                    </div>
                </div>

        </div>

    );
}