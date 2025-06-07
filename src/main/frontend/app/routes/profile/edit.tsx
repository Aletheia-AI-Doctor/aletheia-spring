
import { useUpdateDoctorProfileMutation } from "~/features/doctor/doctorApiSlice";
import React, {useEffect, useState} from "react";
import { useNavigate } from "react-router";
import type { Route } from "./+types/profile";
import Input from "~/components/input";
import Button from "~/components/button";
import {useAppSelector} from "~/base/hooks";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Edit Profile" },
    ];
}

export default function EditProfile() {
    const data = useAppSelector((state) => state.auth.doctor);
    const [updateDoctorProfile, { isLoading: isUpdating }] = useUpdateDoctorProfileMutation();

    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        bio:""
    });
    const navigate = useNavigate();

    useEffect(() => {
        if (data) {
            setFormData({
                name: data.name || "",
                email: data.email || "",
                password: "",
                bio: data.bio || "",
            });
        }
    }, [data]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
    
        const updates: any = {
            name: formData.name, // always send name
            email: formData.email, // always send email
            bio: formData.bio, // always send bio
        };
        
        if (formData.password) {
            updates.password = formData.password;
        }
    
        try {
            await updateDoctorProfile(updates).unwrap();
            alert("Profile updated successfully!");
            console.log('Payload',formData)
            navigate("/profile");
        } catch (err) {
            console.error("Failed to update profile", err);
            alert("An error occurred while updating your profile.");
        }
    };

    return (
        <div className="flex min-h-full flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-lg">
                <h2 className="mt-6 text-center text-2xl font-bold tracking-tight text-gray-900">
                    Edit Your Profile
                </h2>
            </div>

            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-lg">
                <div className="bg-white px-6 py-8 shadow-sm rounded-lg">
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div>
                            <Input
                                id="name"
                                name="name"
                                label="Name"
                                value={formData.name}
                                onChange={(e) => setFormData({...formData, name: e.target.value})}
                                className="text-base py-2.5 px-3.5"
                            />
                            <p className="mt-1.5 text-sm text-gray-500">
                                Leave unchanged to keep current username
                            </p>
                        </div>

                        <div>
                            <Input
                                id="email"
                                name="email"
                                type="email"
                                label="Email"
                                value={formData.email}
                                onChange={(e) => setFormData({...formData, email: e.target.value})}
                                className="text-base py-2.5 px-3.5"
                            />
                            <p className="mt-1.5 text-sm text-gray-500">
                                Leave unchanged to keep current email
                            </p>
                        </div>

                        <div>
                            <Input
                                id="bio"
                                name="bio"
                                type="text"
                                label="Bio"
                                value={formData.bio}
                                onChange={(e) => setFormData({...formData, bio: e.target.value})}
                                className="text-base py-2.5 px-3.5"
                            />
                            <p className="mt-1.5 text-sm text-gray-500">
                                Leave unchanged to keep current bio
                            </p>
                        </div>

                        <div>
                            <Input
                                id="password"
                                name="password"
                                type="password"
                                label="New Password"
                                value={formData.password}
                                onChange={(e) => setFormData({...formData, password: e.target.value})}
                                className="text-base py-2.5 px-3.5"
                            />
                            <p className="mt-1.5 text-sm text-gray-500">
                                Leave blank to keep current password
                            </p>
                        </div>

                        <div className="flex items-center justify-between space-x-4">
                            <Button 
                                type="submit"
                                className="px-6 py-2.5 text-base"
                            >
                                Save Changes
                            </Button>
                            <Button 
                                type="button"
                                className="px-6 py-2.5 text-base bg-gray-400 hover:bg-gray-200 text-gray-800 border border-gray-300"
                                onClick={() => navigate('/profile')}
                            >
                                Cancel
                            </Button>
                            
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}