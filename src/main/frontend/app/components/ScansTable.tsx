import React, {useEffect, useMemo, useState} from "react";
import {
    type GetScansRequest,
    type Scan,
    useGetScansQuery,
} from "~/features/scans/scansApiSlice";
import {type Diagnosis, useGetAllDiagnosesQuery, useChangeDoctorDiagnosisMutation} from "~/features/diagnosis/diagnosisApiSlice";
import If from "~/components/if";
import Loading from "~/components/Loading";
import { Table, Td, Th } from "~/components/Table/table";
import Select from "~/components/select";
import {Link, useSearchParams} from "react-router";
import StyledLink from "~/components/styled-link";

interface ScansTableProps {
    refetchNow?: boolean;
    patientId?: string | number;
}

export default function ScansTable({ refetchNow, patientId }: ScansTableProps) {
    const [selectedDiagnoses, setSelectedDiagnoses] = useState<Record<string, string>>({});
    const [errorMessages, setErrorMessages] = useState<Record<string, string>>({});
    const [isSettingDiagnosis, setIsSettingDiagnosis] = useState<Record<string, boolean>>({});

    const { data: diagnosisList = [], isLoading: isLoadingDiagnoses } = useGetAllDiagnosesQuery();

    const diagnosisByModel = useMemo(() => {
        const modelDiagnosisMap: Record<number, Diagnosis[]> = {};
        diagnosisList.forEach((diagnosis) => {
            if (!modelDiagnosisMap[diagnosis.model.id]) {
                modelDiagnosisMap[diagnosis.model.id] = [];
            }

            modelDiagnosisMap[diagnosis.model.id].push(diagnosis);
        });
        return modelDiagnosisMap;
    }, [diagnosisList]);

    const [params, setParams] = useSearchParams();
    const req: GetScansRequest = {
        page: parseInt(params.get('page') ?? '1'),
    }

    if(patientId) {
        req.patientId = patientId as number;
    }

    const {
        data: pagination,
        isLoading: isLoadingScans,
        refetch,
    } = useGetScansQuery(req);

    const [setDoctorDiagnosis] = useChangeDoctorDiagnosisMutation();

    useEffect(() => {
        if (refetchNow) refetch();
    }, [refetchNow, refetch]);

    const handleDiagnosisChange = async (scanId: string, diagnosisId: string) => {
        setSelectedDiagnoses((prev) => ({
            ...prev,
            [scanId]: diagnosisId,
        }));

        setErrorMessages((prev) => {
            const { [scanId]: _, ...rest } = prev;
            return rest;
        });

        if (!diagnosisId) return;

        setIsSettingDiagnosis((prev) => ({ ...prev, [scanId]: true }));

        try {
            const result = await setDoctorDiagnosis({
                scanId: Number(scanId),
                diagnosis: diagnosisId,
            });

            if (result.error) {
                // @ts-ignore
                const errorMessage = result.error!.data?.error || "Failed to set diagnosis";
                setErrorMessages((prev) => ({
                    ...prev,
                    [scanId]: errorMessage,
                }));
            } else {
                setSelectedDiagnoses((prev) => {
                    const { [scanId]: _, ...rest } = prev;
                    return rest;
                });
                refetch();
            }
        } catch {
            setErrorMessages((prev) => ({
                ...prev,
                [scanId]: "An unexpected error occurred",
            }));
        } finally {
            setIsSettingDiagnosis((prev) => ({ ...prev, [scanId]: false }));
        }
    };

    return (
        <If replacement={<Loading message="Loading scans..." />} condition={!isLoadingScans && !isLoadingDiagnoses && !!pagination}>
            <Table
                pagination={pagination!}
                header={
                    <tr>
                        <Th first>ID</Th>
                        <Th>Image</Th>
                        <Th>Image Response</Th>
                        <Th>Model</Th>
                        <Th>Model Diagnosis</Th>
                        <Th>Doctor Diagnosis</Th>
                        <Th>Patient</Th>
                    </tr>
                }
                body={(item: Scan) => {
                    return (
                        <tr key={item.id}>
                            <Td first>#{item.id}</Td>
                            <Td>
                                <img src={item.imageUrl} alt="Scan" className="w-[100px] h-[100px]" />
                            </Td>
                            <Td>
                                {item.imageResponseUrl ? (
                                    <img src={item.imageResponseUrl} alt="Response" className="w-[100px] h-[100px]" />
                                ) : (
                                    "-"
                                )}
                            </Td>
                            <Td>{item.model?.name ?? "-"}</Td>
                            <Td>{item.modelDiagnosis?.name ?? "-"}</Td>
                            <Td>
                                <div className="flex flex-col gap-2">
                                    <Select
                                        id={`diagnosis-${item.id}`}
                                        value={selectedDiagnoses[item.id] || item.doctorDiagnosis?.id?.toString() || ""}
                                        onChange={(e) =>
                                            handleDiagnosisChange(String(item.id), e.target.value)
                                        }
                                        placeholder="-- Select diagnosis --"
                                        options={
                                            diagnosisByModel[item.model.id].map((d) => ({
                                                label: d.name,
                                                value: String(d.id),
                                            }))
                                        }
                                        disabled={isLoadingDiagnoses || isSettingDiagnosis[String(item.id)] || !item.model?.id}
                                    />
                                    {errorMessages[String(item.id)] && (
                                        <div className="text-red-500 text-sm">{errorMessages[String(item.id)]}</div>
                                    )}
                                </div>
                            </Td>
                            <Td>
                                <StyledLink to={`/patients/${item.patient?.id}`}>
                                {item.patient?.name ?? "-"}
                                </StyledLink>
                            </Td>
                        </tr>
                    );
                }}
            />
        </If>
    );
}