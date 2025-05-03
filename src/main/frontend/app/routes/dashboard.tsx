import React from "react";
import {faHeartPulse, faUserDoctor} from "@fortawesome/free-solid-svg-icons";

const lana_del_rey = {
    id: "222",
    name: "Lana Del Rey",
    totalPatients: 24,
    diagnosed: 16,
    pending: 8
};
const activityLog = [
    "Diagnosed patient John Doe",
    "Added scan for patient Jane Smith",
    "Reviewed MRI result for patient Tom Hardy"
];
export default  function DrDashboard(){
    return(
        <div className="max-w-4xl mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
            <div className="flex items-center space-x-4 mb-6">
                <h1 className="text-4xl font-bold text-blue-600">
                     Hello! Dr. {lana_del_rey.name}
                    <span className="ml-2 text-blue-400 text-lg">MD</span>
                </h1>
            </div>
            <div className="mb-4">
                <div className="border border-gray-200 bg-gray-50 rounded-lg p-4">
                    <p className="text-gray-700 mb-2">
                        <strong>Number of Patients:</strong> {lana_del_rey.totalPatients}
                    </p>
                    <p className="text-gray-700 mb-2">
                        <strong>Diagnosed Patients:</strong> {lana_del_rey.diagnosed}
                    </p>
                    <p className="text-gray-700 mb-2">
                        <strong>Pending Patients:</strong> {lana_del_rey.pending}
                    </p>
                </div>

            </div>
            <div className="mb-4">
                <h2 className="text-2xl font-semibold text-gray-700 mb-2">Activity Log:</h2>
                <div className="border border-gray-200 bg-gray-50 rounded-lg p-4">
                    {activityLog.length > 0 ? (
                        <ul className="list-disc pl-5 text-gray-700">
                            {activityLog.map((activity, index) => (
                                <li key={index}>{activity}</li>
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