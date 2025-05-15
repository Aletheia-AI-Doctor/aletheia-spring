import type {Route} from "./+types/confirm-email";
import {useConfirmEmailMutation} from "~/features/authentication/authenticationApiSlice";
import React, {useEffect} from "react";
import Logo from "~/components/app-logo-2";
import {useNavigate, useNavigation} from "react-router";

export function meta({}: Route.MetaArgs) {
    return [
        {title: "registration"},
    ];
}

export default function confirmedPage(){

    return(
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <Logo className="mb-4" />
            <h1 className="text-4xl font-bold mb-4">confirmation process is done</h1>
            <p className="text-gray-700 mb-4">Your doctor has been successfully confirmed.</p>
        </div>
    )

}