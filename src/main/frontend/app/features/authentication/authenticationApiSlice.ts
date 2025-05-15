import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {createSlice, type PayloadAction} from "@reduxjs/toolkit";
import {getFromLocalStorage, removeFromLocalStorage, setToLocalStorage} from "~/base/helpers";
import { DoctorSpeciality } from '~/features/doctor/doctorSpeciality';


const doctorInit = getFromLocalStorage('doctor');

const initialState : LoginApiResponse = {
    token: getFromLocalStorage('token') || undefined,
    doctor: doctorInit ? (JSON.parse(doctorInit) as Doctor) : undefined,
};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setToken: (state, action: PayloadAction<string>) => {
            state.token = action.payload;
            setToLocalStorage('token', action.payload);
        },
        setDoctor: (state, action : PayloadAction<Doctor>) => {
            state.doctor = action.payload;
            setToLocalStorage('doctor', JSON.stringify(action.payload));
        },
        clearAuth: (state) => {
            state.token = undefined;
            state.doctor = undefined;

            removeFromLocalStorage('token');
            removeFromLocalStorage('doctor');
        },
    },
});

export const {setToken, setDoctor, clearAuth} = authSlice.actions;

export { authSlice };

interface LoginApiResponse {
    token?: string;
    doctor?: Doctor;
    message?: string;
    success?: boolean;
}

interface RegistrationResponse {
    doctor: Doctor;
    message: string;
    success: boolean;
}
interface ConfirmationResponse {
    message: string;
    success: boolean;
}

interface Doctor {
    id: number;
    name: string;
    username: string;
    email: string;
    bio: string;
    specialty: DoctorSpeciality;
    license: string;
    HospitalId: number;
    confirmed?: boolean;
    confirmationToken?: string;
}

interface LoginApiRequest {
    email: string;
    password: string;
}

interface RegisterApiRequest {
    name: string;
    email: string;
    username: string;
    password: string;
    speciality: DoctorSpeciality;
    licenseNumber: string;
    hospitalId: number;

}

// Define a service using a base URL and expected endpoints
export const authenticationApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => {
            headers.set("Content-Type", "application/json");

            return headers;
        },
    }),
    reducerPath: "authenticationApi",
    tagTypes: ["Auth"],
    endpoints: build => ({
        login: build.mutation<LoginApiResponse, LoginApiRequest>({
            query: (req : LoginApiRequest) => {
                return {
                        url: "/api/login",
                        method: "POST",
                        body: {email: req.email, password: req.password},
                    };
            },

        }),
        register: build.mutation<Doctor, RegisterApiRequest>({
            query: (req: RegisterApiRequest) => ({
                url: "/api/register",
                method: "POST",
                body: {
                    name: req.name,
                    email: req.email,
                    username: req.username,
                    password: req.password,
                    speciality: req.speciality,
                    licenseNumber: req.licenseNumber,
                    hospitalId: req.hospitalId,
                },
            }),
            invalidatesTags: ['Auth'],
        }),

        confirmEmail: build.mutation<ConfirmationResponse, { doctorId: string; token: string }>({
            query: ({ doctorId, token }) => ({
                url: `/api/confirm-email/${doctorId}?token=${token}`,
                method: "GET",
            }),
        }),


    }),
});

export const { useLoginMutation, useRegisterMutation, useConfirmEmailMutation} = authenticationApiSlice;