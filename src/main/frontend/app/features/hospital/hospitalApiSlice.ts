import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";

interface Hospital {
    id: number;
    name: string;
    hrEmail: string;
}

interface RegisterHospitalRequest {
    name: string;
    hrEmail: string;
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
                url: "api/hospitals/register",
                method: "POST",
                body,
            }),
        }),
        getHospitals: build.query<Hospital[], void>({
            query: () => "api/hospitals",
            providesTags: ["Hospital"],
        }),
    }),
});

export const { useRegisterMutation, useGetHospitalsQuery } = hospitalApiSlice;
