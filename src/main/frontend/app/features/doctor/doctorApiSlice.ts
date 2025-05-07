import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeaders} from "~/base/helpers";
import { DoctorSpeciality } from '~/features/doctor/doctorSpeciality';


interface Doctor{
    id: number;
    name: string;
    username: string,
    email: string,
    speciality: string,
    bio: string,
}

export type { Doctor };

// Define a service using a base URL and expected endpoints
export const doctorApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => defaultHeaders(headers),
    }),
    reducerPath: "doctorApi",
    tagTypes: ["Doctor"],
    endpoints: build => ({
        getDoctorAttributes: build.query<Doctor, void>({
            query: () => "api/doctors/currentUser",
            providesTags: ['Doctor']
        }),
    }),
})



export const {useGetDoctorAttributesQuery} = doctorApiSlice