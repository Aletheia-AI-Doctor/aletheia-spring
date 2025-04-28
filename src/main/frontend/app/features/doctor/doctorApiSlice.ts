import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeadersFileUpload} from "~/base/helpers";
import { DoctorSpeciality } from '~/DoctorSpeciality';


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
        prepareHeaders: (headers, {}) => defaultHeadersFileUpload(headers),
    }),
    reducerPath: "doctorApi",
    tagTypes: ["Doctor"],
    endpoints: build => ({
        getDoctorAttributes: build.query<Doctor, void>({
            query: () => "api/doctor/me",
            providesTags: (result) => result ? [{ type: 'Doctor', id: result.id }] : ['Doctor'],
        }),
    }),
})



export const {useGetDoctorAttributesQuery} = doctorApiSlice