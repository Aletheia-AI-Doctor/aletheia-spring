import type {Route} from "./+types/registration";
import Input from "~/components/input";
import Button from "~/components/button";
import { DoctorSpeciality } from '~/features/doctor/doctorSpeciality';
import {useGetHospitalsQuery} from "~/features/hospital/hospitalApiSlice";
import {useConfirmEmailMutation} from "~/features/authentication/authenticationApiSlice";
import {useRegisterMutation} from "~/features/authentication/authenticationApiSlice";
import React, {useEffect} from "react";
import Logo from "~/components/app-logo-2";
import {useNavigate, useNavigation} from "react-router";

export function meta({}: Route.MetaArgs) {
    return [
        {title: "registration"},
    ];
}

export default function registration() {

    const [formData, setFormData] = React.useState({
        name: "",
        email: "",
        password: "",
        passwordConfirmation: "",
        speciality: "" as DoctorSpeciality, // Use the enum type
        licence: "",
        hospital: "",
    });
    const [errorMessage, setErrorMessage] = React.useState<string | null>(null);
    const [confirmEmail] = useConfirmEmailMutation();
    // API Queries
    const [register, { isLoading, isSuccess, isError, error, data }] = useRegisterMutation();
    const {
        data: hospitals = [],
        isLoading: hospitalsLoading,
        isError: hospitalsError,
    } = useGetHospitalsQuery();


    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };


    function submit(e: React.FormEvent) {
        e.preventDefault();

        if (formData.password !== formData.passwordConfirmation) {
            setErrorMessage("Passwords don't match");
            return;
        }

        register({
            name: formData.name,
            email: formData.email,
            username: formData.email.split('@')[0],
            password: formData.password,
            speciality: formData.speciality,
            licenseNumber: formData.licence,
            hospitalId: Number(formData.hospital)
        });
    }


    const navigation = useNavigate();
    useEffect(() => {
        if (isSuccess && data) {

            setErrorMessage(null);

            navigation("/login", {replace: true})
        }
        if (isError) {
            setErrorMessage(
                error && 'data' in error
                    ? (error.data as { message?: string })?.message || "Registration failed"
                    : "Registration failed"
            );
        }
    }, [isSuccess, isError, data, error]);
    const specialtyOptions = Object.values(DoctorSpeciality);
    return (
        <>
            <div className="flex min-h-full flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-md">
                    <Logo className="mx-auto h-10 w-auto" />
                    <h2 className="mt-6 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                        create your account
                    </h2>
                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-[480px]">
                    <div className="bg-white px-6 py-12 shadow-sm sm:rounded-lg sm:px-12">
                        <form onSubmit={submit} className="space-y-6">
                            <Input
                                name="name"
                                required
                                label="Full Name"
                                value={formData.name}
                                onChange={handleChange}
                            />
                            <Input
                                name="email"
                                type="email"
                                required
                                label="Email"
                                value={formData.email}
                                onChange={handleChange}
                            />
                            <Input
                                name="password"
                                type="password"
                                required
                                label="Password"
                                value={formData.password}
                                onChange={handleChange}
                            />
                            <Input
                                name="passwordConfirmation"
                                type="password"
                                required
                                label="Confirm Password"
                                value={formData.passwordConfirmation}
                                onChange={handleChange}
                            />
                            <div className="space-y-1">
                                <label className="block text-sm font-medium text-gray-700">
                                    Specialty
                                </label>
                                <select
                                    name="speciality"
                                    value={formData.speciality}
                                    onChange={handleChange}
                                    required
                                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                                >
                                    <option value="">Select specialty</option>
                                    {specialtyOptions.map((specialty) => (
                                        <option key={specialty} value={specialty}>
                                            {specialty}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <Input
                                name="licence"
                                required
                                label="Medical License Number"
                                value={formData.licence}
                                onChange={handleChange}
                            />

                            <div className="space-y-1">
                                <label className="block text-sm font-medium text-gray-700">
                                    Hospital
                                </label>
                                <select
                                    name="hospital"
                                    value={formData.hospital}
                                    onChange={handleChange}
                                    required
                                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                                    disabled={hospitalsLoading}
                                >
                                    <option value="">Select hospital</option>
                                    {hospitals.map((hospital) => (
                                        <option key={hospital.id} value={hospital.id}>
                                            {hospital.name}
                                        </option>
                                    ))}
                                </select>
                                {hospitalsError && (
                                    <p className="mt-2 text-sm text-red-600">Failed to load hospitals</p>
                                )}
                            </div>

                            <div>
                                <Button type="submit" disabled={isLoading}>
                                    {isLoading ? "Loading..." : "Register"}
                                </Button>
                            </div>

                            {errorMessage && <div className="text-red-600 text-sm/6">{errorMessage}</div>}
                        </form>
                    </div>
                </div>
            </div>
        </>
    )
}

