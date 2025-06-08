import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";
import {type PageRequest, type Pagination, queryParamsFromRequest} from "~/types/pagination";

interface Patient {
    id: number;
    name: string;
    sex: string;
    birthdate: string;
    status: string;
    admissionDate: Date;
    scans:[]
}

export type { Patient };

export const patientsApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "patientsApi",
    tagTypes: ["Patients"],
    endpoints: (build) => ({
        getPatients: build.query<Pagination<Patient>, PageRequest>({
            query: (req) => `api/patients` + queryParamsFromRequest(req),
            providesTags: ['Patients'],
        }),
        addPatient: build.mutation<Patient, Partial<Patient>>({
            query: (newPatient) => ({
                url: "api/patients/add",
                method: "PUT",
                body: newPatient,
            }),
            invalidatesTags: ['Patients'],
        }),
        getPatientById: build.query<Patient, string>({
            query: (patientId) => {`api/patients/${{patientId}}/show`
            return `api/patients/${patientId}/show`},
            providesTags: ['Patients'],
        }),
       
        updatestatus: build.mutation<Patient, Partial<Patient>>({
            query: (patient) => ({
                url: `api/patients/${patient.id}/update`,
                method: "PUT",
                body: patient,
            }),
            invalidatesTags: ['Patients'],
        }),

    }),
});

export const { useGetPatientsQuery, useAddPatientMutation,useGetPatientByIdQuery,useUpdatestatusMutation } = patientsApiSlice;