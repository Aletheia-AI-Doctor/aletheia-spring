import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeartPulse, faUserDoctor } from '@fortawesome/free-solid-svg-icons';
import React from "react";
import type { Route } from "./+types/profile";
import {useAppSelector} from "~/base/hooks";
import Loading from "~/components/Loading";
import Card from "~/components/Card";
import Modal from "~/components/modal";
import Button from "~/components/button";
import EditProfile from "~/components/edit-profile";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Profile" },
        { name: "description", content: "Welcome to React Router!" },
    ];
}

export default function DrProfilePage(){
    const doctor = useAppSelector((state) => state.auth.doctor);
    const [editing, setEditing] = React.useState(false);

    if (!doctor) {
        return <Loading />;
    }

    return (
        <Card className="max-w-2xl mx-auto">
            <div className="flex items-center space-x-4 mb-6">
                <img src={doctor.image} alt={`${doctor.name}'s Picture`} className="rounded-full size-16" />
                <div>
                    <h1 className="text-3xl font-bold text-gray-800">
                        Dr. {doctor.name}
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
                <Button
                    width="w-auto"
                    onClick={() => setEditing(true)}
                >
                    Edit Profile
                </Button>
                    <Modal open={editing} onClose={() => setEditing(false)}>
                        <EditProfile onCancel={() => setEditing(false)} />
                    </Modal>
            </div>
        </Card>
    );
}