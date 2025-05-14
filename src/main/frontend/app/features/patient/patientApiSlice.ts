import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";

interface Patient {
    name: string;
    sex: string;
    Birthday: string;
    status: string;
}
interface Scan {
  id: string;
  title: string;
  aiDiagnosis: string;
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
            query: () => "api/patients",
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
            console.log(`api/patients/${patientId}/show`)
            return `api/patients/${patientId}/show`},
            providesTags: ['Patients'],
        }),
    }),
});

export const { useGetPatientsQuery, useAddPatientMutation,useGetPatientByIdQuery } = patientsApiSlice;