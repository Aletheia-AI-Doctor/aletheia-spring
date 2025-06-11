import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";

interface Diagnosis {
    modelId: number;
    name: string;
    id: number; // Add id to match DiagnosisDto
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
        getAllDiagnoses: build.query<Diagnosis[], { modelId: number }>({
            query: ({ modelId }) => `api/diagnoses/byModel?modelId=${modelId}`,
            providesTags: ["Diagnosis"],
        }),
    }),
});

export const { useGetAllDiagnosesQuery } = diagnosisApiSlice;