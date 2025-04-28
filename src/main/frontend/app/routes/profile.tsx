import React from 'react';
import { useGetDoctorAttributesQuery } from "~/features/scans/doctorApiSlice";

export default function DrProfilePage() {
        const {
                data: doctor,
                isLoading,
                isError,
                error
        } = useGetDoctorAttributesQuery();

        if (isLoading) {
                return <div className="container mx-auto p-4">Loading profile...</div>;
        }
        if (isError) {
                return (
                    <div className="container mx-auto p-4 text-red-500">
                            Error loading profile: {'data' in error ? error.data : 'Unknown error'}
                    </div>
                );
        }
return (
    <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Doctor Profile</h1>
            {doctor && (
                <div className="space-y-2">
                        <p><strong>Name:</strong> {doctor.name}</p>
                        <p><strong>Username:</strong> {doctor.username}</p>
                        <p><strong>Email:</strong> {doctor.email}</p>
                        <p><strong>Specialty:</strong> {doctor.specialty}</p>
                        <p><strong>Bio:</strong> {doctor.bio}</p>
                </div>
            )}
    </div>
);


}