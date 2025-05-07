import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeaders} from "~/base/helpers";

interface doctorPatients{
    total: number;
    pending: number;
    diagnosed: number,
}

export type { doctorPatients };

// Define a service using a base URL and expected endpoints
export const doctorDashboardApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => defaultHeaders(headers),
    }),
    reducerPath: "doctorDashboardApi",
    tagTypes: ["doctorPatients"],
    endpoints: build => ({
        getDoctorPatients: build.query<doctorPatients, void>({
            query: () => "api/doctors/patientsCount",
            providesTags: ['doctorPatients'],
        }),
    }),
})



export const {useGetDoctorPatientsQuery} = doctorDashboardApiSlice