import type {Route} from "./+types/registration";
import Input from "~/components/input";
import Button from "~/components/button";
import { DoctorSpeciality } from '~/features/doctor/doctorSpeciality';
import {useGetHospitalsQuery} from "~/features/hospital/hospitalApiSlice";
import {useRegisterMutation} from "~/features/authentication/authenticationApiSlice";
import React, {useEffect} from "react";
import Logo from "~/components/app-logo-2";
import {useNavigate} from "react-router";
import Select from "~/components/select";
import {useAppDispatch} from "~/base/hooks";
import {addError, clearAllErrors} from "~/features/errors/errorSlice";
import {sendSuccessNotification} from "~/features/notifications/notificationSlice";

export function meta({}: Route.MetaArgs) {
    return [
        {title: "Create Account - Aletheia"},
    ];
}

export default function registration() {

    const [formData, setFormData] = React.useState({
        name: "",
        email: "",
        username: "",
        password: "",
        passwordConfirmation: "",
        speciality: "" as DoctorSpeciality,
        licenseNumber: "",
        hospital: "",
    });

    const [register, { isLoading, isSuccess, error, data }] = useRegisterMutation();
    const {
        data: hospitals = [],
        isLoading: hospitalsLoading,
        isError: hospitalsError,
    } = useGetHospitalsQuery();

    const dispatch = useAppDispatch();


    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };


    function submit(e: React.FormEvent) {
        e.preventDefault();

        if (formData.password !== formData.passwordConfirmation) {
            dispatch(addError({
                key: "passwordConfirmation",
                message: "Passwords do not match."
            }));
            return;
        }

        register({
            name: formData.name,
            email: formData.email,
            username: formData.username,
            password: formData.password,
            speciality: formData.speciality,
            licenseNumber: formData.licenseNumber,
            hospitalId: Number(formData.hospital)
        });
    }


    const navigation = useNavigate();
    useEffect(() => {
        if (isSuccess && data) {
            dispatch(sendSuccessNotification("Registration successful! An email has been sent to verify your account."));
            dispatch(clearAllErrors());

            navigation("/login", {replace: true})
        }
    }, [isSuccess, data, error]);
    const specialtyOptions = Object.values(DoctorSpeciality);
    return (
        <>
            <div className="flex min-h-full flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-md">
                    <Logo className="mx-auto h-20 w-auto" />
                    <h2 className="mt-6 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                        Create Your Account
                    </h2>
                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-[480px]">
                    <div className="bg-white px-6 py-12 shadow-sm sm:rounded-lg sm:px-12">
                        <form onSubmit={submit} className="space-y-6">
                            <Input
                                id="name"
                                name="name"
                                required
                                label="Full Name"
                                value={formData.name}
                                onChange={handleChange}
                            />
                            <Input
                                id="username"
                                name="username"
                                autoComplete="off"
                                required
                                label="Username"
                                value={formData.username}
                                onChange={handleChange}
                            />
                            <Input
                                id="email"
                                name="email"
                                type="email"
                                required
                                label="Email"
                                value={formData.email}
                                onChange={handleChange}
                            />
                            <Input
                                id="password"
                                name="password"
                                type="password"
                                required
                                label="Password"
                                value={formData.password}
                                onChange={handleChange}
                            />
                            <Input
                                id="passwordConfirmation"
                                name="passwordConfirmation"
                                type="password"
                                required
                                label="Confirm Password"
                                value={formData.passwordConfirmation}
                                onChange={handleChange}
                            />
                            <div className="space-y-1">
                                <Select
                                    id="speciality"
                                    name="speciality"
                                    required
                                    label="Specialty"
                                    placeholder="Select speciality"
                                    disabled={hospitalsLoading}
                                    value={formData.speciality}
                                    onChange={handleChange}
                                    options={specialtyOptions.map((specialty) => ({
                                        value: specialty,
                                        label: specialty,
                                    }))}
                                />
                            </div>
                            <Input
                                id="licenseNumber"
                                name="licenseNumber"
                                required
                                label="Medical License Number"
                                value={formData.licenseNumber}
                                onChange={handleChange}
                            />

                            <div className="space-y-1">
                                <Select
                                    id="hospital"
                                    label="Hospital"
                                    name="hospital"
                                    required
                                    placeholder="Select hospital"
                                    disabled={hospitalsLoading}
                                    value={formData.hospital}
                                    onChange={handleChange}
                                    options={hospitals.map((hospital) => ({
                                        value: hospital.id.toString(),
                                        label: hospital.name,
                                    }))}
                                />
                                {hospitalsError && (
                                    <p className="mt-2 text-sm text-red-600">Failed to load hospitals</p>
                                )}
                            </div>

                            <div>
                                <Button type="submit" disabled={isLoading}>
                                    {isLoading ? "Loading..." : "Register"}
                                </Button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </>
    )
}

