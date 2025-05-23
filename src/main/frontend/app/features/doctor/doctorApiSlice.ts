// features/doctor/doctorApiSlice.ts
import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {ROOT_URL} from "~/base/consts";
import {defaultHeaders} from "~/base/helpers";
import {DoctorSpeciality} from '~/features/doctor/doctorSpeciality';


interface Doctor {
    id: number;
    name: string;
    username: string;
    email: string;
    speciality: DoctorSpeciality;
    bio: string;
}

interface DoctorUpdate {
    name: string;
    email: string;
    password?: string;
    bio: string;

}

interface ActivityLog {
    id: number;
    action: string;
    description: string;
    createdAt: string;
}


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

        getDoctorActivityLog: build.query<ActivityLog[], void>({
            query: () => "api/activities"
        }),

    }),


});


export const {
    useGetDoctorAttributesQuery,
    useUpdateDoctorProfileMutation,
    useGetDoctorActivityLogQuery

} = doctorApiSlice;