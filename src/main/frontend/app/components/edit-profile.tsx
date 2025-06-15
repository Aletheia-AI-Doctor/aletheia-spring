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
        bio: "",
        username: ""
    });

    useEffect(() => {
        if (data) {
            setFormData({
                name: data.name || "",
                email: data.email || "",
                password: "",
                bio: data.bio || "",
                username: data.username || ""
            });
        }
    }, [data]);
    const dispatch = useAppDispatch();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
    
        const updates: any = {
            name: formData.name,
            email: formData.email,
            username: formData.username,
            bio: formData.bio,
        };
        
        if (formData.password) {
            updates.password = formData.password;
        }
    

        const response = await updateDoctorProfile(updates);
        if(response.error) {
            return;
        }

        alert("Profile updated successfully!");

        onCancel();

        dispatch(setDoctor(response.data));
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
                            required
                            value={formData.name}
                            onChange={(e) => setFormData({...formData, name: e.target.value})}
                        />
                    </div>

                    <div>
                        <Input
                            id="username"
                            name="username"
                            required
                            label="Username"
                            value={formData.username}
                            onChange={(e) => setFormData({...formData, username: e.target.value})}
                        />
                        <Input
                            id="email"
                            name="email"
                            type="email"
                            required
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