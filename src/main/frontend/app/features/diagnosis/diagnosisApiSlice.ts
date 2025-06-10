import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeaders} from "~/base/helpers";

interface diagnosis {
    modelId: number;
    name: string;
}

export type {diagnosis};

// Define a service using a base URL and expected endpoints
export const diagnosisApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => defaultHeaders(headers),
    }),
    reducerPath: "doctorActivityApi",
    tagTypes: ["diagnosis"],
    keepUnusedDataFor: 2,
    endpoints: build => ({

        getAllDiagnoses: build.query<diagnosis[], void>({
            query: () => "api/diagnoses",
            providesTags: ["diagnosis"],
        }),
    }),
})



export const {useGetAllDiagnoses} = diagnosisApiSlice