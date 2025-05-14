import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";

interface Hospital {
    id: number;
    name: string;
    hr_email: string;
}

interface RegisterHospitalRequest {
    name: string;
    hr_email: string;
}

export const hospitalApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "hospitalApi",
    tagTypes: ["Hospital"],
    endpoints: (build) => ({
        register: build.mutation<Hospital, RegisterHospitalRequest>({
            query: (body) => ({
                url: "api/hospital/register",
                method: "POST",
                body,
            }),
            // invalidatesTags: ['Hospital'] // Uncomment if needed
        }),
    }),
});

export const { useRegisterMutation } = hospitalApiSlice;
