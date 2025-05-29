// features/doctor/doctorApiSlice.ts
import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ROOT_URL} from "~/base/consts";
import {defaultHeaders} from "~/base/helpers";
import type {Doctor} from '~/features/authentication/authenticationApiSlice';


export const doctorApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "doctorApi",
    tagTypes: ["Doctor"],
    endpoints: (build) => ({
        getDoctorAttributes: build.query<Doctor, void>({
            query: () => "api/doctors/currentUser",
            providesTags: ['Doctor']
        }),

        updateDoctorProfile: build.mutation<Doctor, { name: string, email: string, password: string, bio:string}>({
            query: (updates) => ({
                url: "api/doctors/update",
                method: "PUT",
                body: updates
            }),
            invalidatesTags: ['Doctor']
        }),

    }),


});


export const {
    useGetDoctorAttributesQuery,
    useUpdateDoctorProfileMutation
} = doctorApiSlice;