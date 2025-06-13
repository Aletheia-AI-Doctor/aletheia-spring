import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";
import type {Model} from "~/features/scans/scansApiSlice";

interface Diagnosis {
    model: Model;
    name: string;
    id: number;
}

export type { Diagnosis };

export const diagnosisApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => defaultHeaders(headers),
    }),
    reducerPath: "diagnosisApi",
    tagTypes: ["Diagnosis"],
    keepUnusedDataFor: 2,
    endpoints: build => ({
        getAllDiagnoses: build.query<Diagnosis[], void>({
            query: () => `api/diagnoses`,
            providesTags: ["Diagnosis"],
        }),

        changeDoctorDiagnosis: build.mutation<void, { scanId: number, diagnosis: string }>({
            query: ({ scanId, diagnosis }) => ({
                url: `api/scans/${scanId}/doctor-diagnosis`,
                method: "PATCH",
                body: { diagnosis },
            }),
        }),
    }),
});

export const { useGetAllDiagnosesQuery, useChangeDoctorDiagnosisMutation } = diagnosisApiSlice;