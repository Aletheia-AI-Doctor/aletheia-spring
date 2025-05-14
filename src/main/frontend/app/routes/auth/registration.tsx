import type {Route} from "./+types/registration";
import Input from "~/components/input";
import Button from "~/components/button";
import {
    useRegisterMutation,
    setDoctor,
    setToken,
} from "~/features/authentication/authenticationApiSlice";
import React, {useEffect} from "react";
import {useAppDispatch} from "~/base/hooks";
import Logo from "~/components/app-logo-icon";

export function meta({}: Route.MetaArgs) {
    return [
        {title: "registration"},
    ];
}

export default function registration() {

    const [registration, {isLoading, isSuccess, isError, error, data}] = useRegisterMutation();
    const [name, setName] = React.useState("");
    const [email, setEmail] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [passwordConfirmation, setPasswordConfirmation] = React.useState("");
    const[speciality, setSpeciality] = React.useState("");
    const[licence, setLicense] = React.useState("");
    const[Hospital, setHospital] = React.useState("");
    const [errorMessage, setErrorMessage] = React.useState<string | null>(null);

    const dispatch = useAppDispatch();

    function submit(e: React.FormEvent) {
        e.preventDefault();

        registration({email, password});
    }

    useEffect(() => {
        if (isSuccess && data) {
            dispatch(setToken(data.token!));
            dispatch(setDoctor(data.doctor!));

            setErrorMessage(null);

            window.location.href = "/";
        }
        if(isError && error) {
            // @ts-ignore
            setErrorMessage(error.data?.message);
        }
    }, [isSuccess, isLoading]);

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
                                id="name"
                                name="name"
                                required={true}
                                label="Name"
                                value={name}
                                onChange={e => setName(e.target.value)}
                            />
                            <Input
                                id="email"
                                name="email"
                                required={true}
                                label="Email or Username"
                                value={email}
                                onChange={e => setEmail(e.target.value)}
                            />

                            <Input
                                id="password"
                                name="password"
                                type="password"
                                required={true}
                                label="Password"
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                            />
                            <Input
                                id="passwordConfirmation"
                                name="passwordConfirmation"
                                type="password"
                                required={true}
                                label="Password Confirmation"
                                value={passwordConfirmation}
                                onChange={e => setPasswordConfirmation(e.target.value)}
                            />
                            <Input
                                id="speciality"
                                name="speciality"
                                required={true}
                                label="Speciality"
                                value={speciality}
                                onChange={e => setSpeciality(e.target.value)}
                            />
                            <Input
                                id="licence"
                                name="licence"
                                required={true}
                                label="Licence"
                                value={licence}
                                onChange={e => setLicense(e.target.value)}
                            />
                            <Input
                                id="Hospital"
                                name="Hospital"
                                required={true}
                                label="Hospital"
                                value={Hospital}
                                onChange={e => setHospital(e.target.value)}
                            />

                            <div>
                                <Button type="submit" disabled={isLoading}>
                                    {isLoading ? "Loading..." : "Sign In"}
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

