import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeadersFileUpload} from "~/base/helpers";
import type {Patient} from "~/features/patient/patientApiSlice";
import {type PageRequest, type Pagination, queryParamsFromRequest} from "~/types/pagination";

interface Diagnosis {
    id: number;
    name: string;
    imagePath: string;
    imageResponsePath: string;
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
        }),

        saveScan: build.mutation<void, SaveScanApiRequest>({
            query: (req) => ({
                url: `api/scans`,
                method: "POST",
                body: req,
            }),
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
                    })),
                };
            },
        }),
    }),
})



export const {useUploadScanMutation, useGetModelsQuery, useSaveScanMutation, useGetScansQuery} = scansApiSlice