import { useGetDoctorAttributesQuery } from "~/features/doctor/doctorApiSlice";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faHeartPulse, faUserDoctor} from '@fortawesome/free-solid-svg-icons';
import React from "react";
const lana_del_rey = {
    id: "222",
    name: "Lana Del Rey",
    username: "LDR_love",
    email: "insane@IHeart.com",
    specialty: "Cardiology",
    bio: "Board-certified cardiologist with an amazing soul, and a perfect sass."
};

export default function DrProfilePage() {
    /*const {
        data: doctor,
        isLoading,
        isError,
        error
    } = useGetDoctorAttributesQuery();

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError) {
        // Handle error response properly
        const errorData = error as { data?: { title?: string, detail?: string } };
        return (
            <div className="text-red-500">
                <h3>Error loading profile</h3>
                <p>{errorData.data?.title || 'Unknown error'}</p>
                {errorData.data?.detail && <p>{errorData.data.detail}</p>}
            </div>
        );
    }

    if (!doctor) {
        return <div>No doctor data found</div>;
    }*/

    return (
        <div className="max-w-2xl mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
            <div className="flex items-center space-x-4 mb-6">
                <div className="p-3 rounded-full bg-blue-50 text-blue-600">
                    <FontAwesomeIcon icon={faUserDoctor} size="3x" />
                </div>
                <h1 className="text-3xl font-bold text-gray-800">
                    Dr. {lana_del_rey.name}
                    <span className="ml-2 text-blue-600 text-lg">  MD</span>
                </h1>
            </div>
            <div className="mb-6">
                <p className="text-gray-600 bg-gray-50 p-4 rounded-lg border border-gray-200">
                    {lana_del_rey.bio}
                </p>
            </div>
            <div className="bg-blue-50 rounded-lg p-5 mb-6 border-l-4 border-blue-400">
                <div className="space-y-4">
                    <div className="flex items-center">
                        <p className="text-gray-700">
                            <span className="font-semibold">Username:</span> @{lana_del_rey.username}
                        </p>
                    </div>

                    <div className="flex items-center space-x-3">
                        <p className="text-gray-700">
                            <span className="font-semibold">Specialty:</span> {lana_del_rey.specialty}
                        </p>

                        <FontAwesomeIcon
                            icon={faHeartPulse}
                            className="text-red-400 text-lg"
                        />
                    </div>

                    <div className="flex items-center">
                        <p className="text-gray-700">
                            <span className="font-semibold">Email:</span> {lana_del_rey.email}
                        </p>
                    </div>
                </div>
            </div>
            <div className="flex space-x-3">
                <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition flex items-center">
                    Edit Profile
                </button>
            </div>
        </div>
    );
}