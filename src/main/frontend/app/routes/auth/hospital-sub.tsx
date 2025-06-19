import type { Route } from "./+types/hospital-sub";
import { useNavigate } from "react-router";
import Input from "~/components/input";
import Button from "~/components/button";
import { useRegisterMutation } from "~/features/hospital/hospitalApiSlice";
import React, { useEffect } from "react";
import { useAppDispatch } from "~/base/hooks";
import Logo from "~/components/app-logo-icon";
import {sendSuccessNotification} from "~/features/notifications/notificationSlice";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "hospital subscription" },
    ];
}

export default function HospitalSubscription() {
    const navigate = useNavigate();
    const [register, { isLoading, isSuccess, isError, error, data }] = useRegisterMutation();
    const [name, setName] = React.useState("");
    const [hrEmail, setHrEmail] = React.useState("");
    const dispatch = useAppDispatch();

    const submit = (e: React.FormEvent) => {
        e.preventDefault();
        register({ name, hrEmail });
    };

    useEffect(() => {
        if (isSuccess && data) {
            dispatch(sendSuccessNotification("Hospital registered successfully!"));

            navigate("/", { replace: true });
        }
    }, [isSuccess, isError, data, error, dispatch, navigate]);

    return (
        <div className="container mx-auto p-4">
            <Logo className="mx-auto mb-8" />
            <form onSubmit={submit} className="max-w-md mx-auto">
                <h1 className="text-2xl font-bold mb-6">Hospital Registration</h1>

                <Input
                    id="name"
                    name="name"
                    label="Hospital Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                    className="mb-4"
                />

                <Input
                    id="email"
                    label="HR Email"
                    name="hrEmail"
                    type="email"
                    value={hrEmail}
                    onChange={(e) => setHrEmail(e.target.value)}
                    required
                    className="mb-6"
                />

                <Button type="submit" disabled={isLoading}>
                    {isLoading ? "Registering..." : "Register Hospital"}
                </Button>
            </form>
        </div>
    );
}