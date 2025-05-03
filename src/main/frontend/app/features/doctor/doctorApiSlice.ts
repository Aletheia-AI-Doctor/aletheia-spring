import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeaders, defaultHeadersFileUpload} from "~/base/helpers";
import { DoctorSpeciality } from '~/features/doctor/doctorSpeciality';


interface Doctor{
    id: string;
    name: string;
    username: string,
    email: string,
    specialty: DoctorSpeciality
    bio: string

}

export type { Doctor };

// Define a service using a base URL and expected endpoints
export const doctorApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        credentials: 'include',
        prepareHeaders: (headers, {}) => defaultHeaders(headers),
    }),
    reducerPath: "doctorApi",
    tagTypes: ["Doctor"],
    endpoints: (builder) => ({
        getDoctorAttributes: builder.query<Doctor, void>({
            query: () => "api/doctors/currentUser",
            providesTags: ['Doctor']
        }),
    }),
})



export const {useGetDoctorAttributesQuery} = doctorApiSlice