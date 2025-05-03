import React from "react";
import {faHeartPulse, faUserDoctor} from "@fortawesome/free-solid-svg-icons";

const lana_del_rey = {
    id: "222",
    name: "Lana Del Rey",
    username: "LDR_love",
    email: "insane@IHeart.com",
    specialty: "Cardiology",
    bio: "Board-certified cardiologist with an amazing soul, and a perfect sass."
};

export default  function DrDashboard(){
    return(
        <div className="max-w-2xl mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
            <div className="flex items-center space-x-4 mb-6">
                <div className="p-3 rounded-full bg-blue-50 text-blue-600">
                    <FontAwesomeIcon icon={faUserDoctor} size="3x" />
                </div>
                <h1 className="text-3xl font-bold text-gray-800">
                    Dr. {doctor.name}
                    <span className="ml-2 text-blue-600 text-lg">MD</span>
                </h1>
            </div>
            <div className="mb-6">
                <p className="text-gray-600 bg-gray-50 p-4 rounded-lg border border-gray-200">
                    {doctor.bio}
                </p>
            </div>
            <div className="bg-blue-50 rounded-lg p-5 mb-6 border-l-4 border-blue-400">
                <div className="space-y-4">
                    <div className="flex items-center">
                        <p className="text-gray-700">
                            <span className="font-semibold">Username:</span> @{doctor.username}
                        </p>
                    </div>

                    <div className="flex items-center space-x-3">
                        <p className="text-gray-700">
                            <span className="font-semibold">Specialty:</span> {doctor.specialty}
                        </p>

                        <FontAwesomeIcon
                            icon={faHeartPulse}
                            className="text-red-400 text-lg"
                        />
                    </div>

                    <div className="flex items-center">
                        <p className="text-gray-700">
                            <span className="font-semibold">Email:</span> {doctor.email}
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