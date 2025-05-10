import { useGetDoctorAttributesQuery } from "~/features/doctor/doctorApiSlice";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeartPulse, faUserDoctor } from '@fortawesome/free-solid-svg-icons';
import React from "react";
import { useNavigate } from "react-router";
import type { Route } from "./+types/profile";


import Loading from "~/components/Loading";
import EditProfile from "./edit";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Profile" },
        { name: "description", content: "Welcome to React Router!" },
    ];
}


export default function DrProfilePage(){
    const navigate = useNavigate();
    const {
        data: doctor,
        isLoading,
        isError,
        error
    } = useGetDoctorAttributesQuery();

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    console.log(doctor, "Doctor Data");

    if (isError) {
        console.error('Error fetching doctor:', error);
        if (error.status === 403) {
            // Redirect to login if unauthorized
            navigate('/login');
            return null;
        }
        return (
            <div className="max-w-2xl mx-auto p-6 text-red-500">
                Error loading profile. Please try again later.
            </div>
        );
    }

    return (
        <div className="max-w-2xl mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
            <div className="flex items-center space-x-4 mb-6">
                <div className="p-3 rounded-full bg-blue-50 text-blue-600">
                    <FontAwesomeIcon icon={faUserDoctor} size="3x" />
                </div>
                <div>
                    <h1 className="text-3xl font-bold text-gray-800">
                        Dr. {doctor.name}
                        <span className="ml-2 text-blue-600 text-lg">MD</span>
                    </h1>
                    <p className="text-gray-500">@{doctor.username}</p>
                </div>
            </div>

            <div className="mb-6">
                <h2 className="text-xl font-semibold text-gray-800 mb-2">About</h2>
                <p className="text-gray-600 bg-gray-50 p-4 rounded-lg border border-gray-200">
                    {doctor.bio || "No biography available"}
                </p>
            </div>

            <div className="bg-blue-50 rounded-lg p-5 mb-6 border-l-4 border-blue-400">
                <h2 className="text-xl font-semibold text-gray-800 mb-3">Professional Details</h2>
                <div className="flex items-center">
                    <p className="text-gray-700">
                        <span className="font-semibold">Hospital:</span> {"Louran Hospital"}
                    </p>
                </div>
                <div className="space-y-4">
                    <div className="flex items-center space-x-3">
                        <p className="text-gray-700">
                            <span className="font-semibold">Speciality:</span> {doctor.speciality || "Not specified"}
                        </p>
                        <FontAwesomeIcon icon={faHeartPulse} className="text-red-400 text-lg" />
                    </div>

                    <div className="flex items-center">
                        <p className="text-gray-700">
                            <span className="font-semibold">Email:</span> {doctor.email}
                        </p>
                    </div>
                </div>
            </div>

            

            <div className="flex space-x-3">
                
                <button
                    className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition flex items-center"
                    onClick={() => navigate('/profile/edit')}
                >
                    Edit Profile
                </button>
            </div>
        </div>
    );
}