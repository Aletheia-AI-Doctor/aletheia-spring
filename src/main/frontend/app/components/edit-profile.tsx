import { useUpdateDoctorProfileMutation } from "~/features/doctor/doctorApiSlice";
import React, {useEffect, useState} from "react";
import Input from "~/components/input";
import Button from "~/components/button";
import {useAppDispatch, useAppSelector} from "~/base/hooks";
import {setDoctor} from "~/features/authentication/authenticationApiSlice";


export default function EditProfile({onCancel} : {onCancel: () => void}) {
    const data = useAppSelector((state) => state.auth.doctor);
    const [updateDoctorProfile, { isLoading: isUpdating }] = useUpdateDoctorProfileMutation();

    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        bio:""
    });

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
    const dispatch = useAppDispatch();

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
            const response = await updateDoctorProfile(updates);
            if(response.error) {
                throw new Error(response.error.data.message || "Failed to update profile");
            }

            alert("Profile updated successfully!");
            console.log('Payload',formData)
            onCancel();
            dispatch(setDoctor(response.data));
        } catch (err) {
            console.error("Failed to update profile", err);
            alert("An error occurred while updating your profile.");
        }
    };

    return (
        <div>

            <div>
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <Input
                            id="name"
                            name="name"
                            label="Name"
                            value={formData.name}
                            onChange={(e) => setFormData({...formData, name: e.target.value})}
                        />
                    </div>

                    <div>
                        <Input
                            id="email"
                            name="email"
                            type="email"
                            label="Email"
                            value={formData.email}
                            onChange={(e) => setFormData({...formData, email: e.target.value})}
                        />
                    </div>

                    <div>
                        <Input
                            id="bio"
                            name="bio"
                            type="text"
                            label="Bio"
                            value={formData.bio}
                            onChange={(e) => setFormData({...formData, bio: e.target.value})}
                        />
                    </div>

                    <div>
                        <Input
                            id="password"
                            name="password"
                            type="password"
                            label="New Password"
                            value={formData.password}
                            onChange={(e) => setFormData({...formData, password: e.target.value})}
                        />
                    </div>

                    <div className="flex items-center justify-between space-x-4">
                        <Button
                            type="submit"
                        >
                            Save Changes
                        </Button>
                        <Button
                            type="button"
                            color="gray"
                            onClick={() => onCancel()}
                        >
                            Cancel
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
}