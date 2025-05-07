import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";

interface Patient {
    id: number;
    name: string;
    sex: string;
    admissionDate: string;
    status: string;
}

export const patientsApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "patientsApi",
    tagTypes: ["Patients"],
    endpoints: (build) => ({
        getPatients: build.query<Patient[], void>({
            query: () => "api/patient",
            providesTags: ['Patients'],
        }),
        addPatient: build.mutation<Patient, Partial<Patient>>({
            query: (newPatient) => ({
                url: "api/patient/add",
                method: "POST",
                body: newPatient,
            }),
            invalidatesTags: ['Patients'],
        }),
    }),
});

export const { useGetPatientsQuery, useAddPatientMutation } = patientsApiSlice;

