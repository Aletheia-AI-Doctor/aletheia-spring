import {type BaseQueryMeta, createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeadersFileUpload} from "~/base/helpers";
import type {Patient} from "~/features/patient/patientApiSlice";
import {type PageRequest, type Pagination, queryParamsFromRequest} from "~/types/pagination";

interface Diagnosis {
    id: number;
    name: string;
    imagePath: string;
    imageUrl?: string;
    imageResponsePath: string;
    imageResponseUrl?: string;
}
interface Model {
    id: string;
    name: string;
    path: string;
    slug: string;
}

interface SaveScanApiRequest {
    patientId?: number;
    modelDiagnosis: string;
    imageResponsePath?: string;
    doctorDiagnosis?: string;
    imagePath: string;
    model: string;
}

interface Diagnosis {
    id: number;
    name: string;
}

interface Scan {
    id: number;
    patient?: Patient;
    modelDiagnosis: Diagnosis;
    doctorDiagnosis?: Diagnosis;
    imageUrl: string;
    imageResponseUrl: string;
    model: Model;
}

export type { Diagnosis, Model, Scan };

type GetScansRequest = PageRequest & {
    patientId?: number;
};

// Define a service using a base URL and expected endpoints
export const scansApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => defaultHeadersFileUpload(headers),
    }),
    reducerPath: "scansApi",
    tagTypes: ["Scans", "Models"],
    endpoints: build => ({

        getModels: build.query<Model[], void>({
            query: () => "api/models",
            providesTags: ['Models'],
        }),

        uploadScan: build.mutation<Diagnosis, { scan: File, model:string }>({
            query: ({scan, model}) => {

                const formData = new FormData();
                formData.append("scan", scan);

                return {
                    url: `api/models/${model}/predict`,
                    method: "PUT",
                    headers: {
                        "Content-Type": "multipart/form-data;"
                    },
                    body: formData,
                    formData: true,
                };
            },
            transformResponse: (response: Diagnosis, meta, arg) => {
                return {
                    ...response,
                    imageResponseUrl: `${ROOT_URL}/api/scans/${response.imageResponsePath}/image`,
                };
            }
        }),

        saveScan: build.mutation<void, SaveScanApiRequest>({
            query: (req) => ({
                url: `api/scans`,
                method: "POST",
                body: req,
            }),
        }),

        setDoctorDiagnosis: build.mutation<void, { scanId: number, doctorDiagnosis: string }>({
            query: ({scanId, doctorDiagnosis}) => ({
                url: `api/scans/${scanId}/setDoctorDiagnosis`,
                method: "PUT",
                body: {doctorDiagnosis},
            }),
            invalidatesTags: ['Scans'],
        }),

        getScans: build.query<Pagination<Scan>, GetScansRequest>({
            query: (req) => {
                const queryParams: any[] = [];
                if (req.patientId) {
                    queryParams.push(`patientId=${req.patientId}`);
                }

                return `api/scans` + queryParamsFromRequest(req, queryParams);
            },
            providesTags: ['Scans'],
            transformResponse: (response: Pagination<Scan>, meta, arg) => {
                return {
                    ...response,
                    data: response.data.map(item => ({
                        ...item,
                        imageUrl: ROOT_URL + item.imageUrl,
                        imageResponseUrl: item.imageResponseUrl ? ROOT_URL + item.imageResponseUrl : "",
                    })),
                };
            },
        }),
    }),
})



export const {useUploadScanMutation, useGetModelsQuery, useSaveScanMutation, useGetScansQuery, useSetDoctorDiagnosis} = scansApiSlice