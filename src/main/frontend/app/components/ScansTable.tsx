import React, { useEffect, useState } from "react";
import {
    type Scan,
    useGetScansQuery,
    useSetDoctorDiagnosisMutation,
} from "~/features/scans/scansApiSlice";
import { useGetAllDiagnosesQuery } from "~/features/diagnosis/diagnosisApiSlice";

import If from "~/components/if";
import Loading from "~/components/Loading";
import { Table, Td, Th } from "~/components/Table/table";
import Select from "~/components/select";

interface ScansTableProps {
    refetchNow?: boolean;
    patientId?: string | number;
}

export default function ScansTable({ refetchNow, patientId }: ScansTableProps) {
    const [selectedDiagnoses, setSelectedDiagnoses] = useState<Record<string, string>>({});
    const [errorMessages, setErrorMessages] = useState<Record<string, string>>({});
    const [isSettingDiagnosis, setIsSettingDiagnosis] = useState<Record<string, boolean>>({});

    const requestParams = {
        page: 1,
        ...(patientId ? { patientId } : {}),
    };

    const {
        data: pagination,
        isLoading: isLoadingScans,
        refetch,
    } = useGetScansQuery(requestParams);

    const {
        data: diagnosisList = [],
        isLoading: isLoadingDiagnoses,
    } = useGetAllDiagnosesQuery();

    const [setDoctorDiagnosis] = useSetDoctorDiagnosisMutation();

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
                doctorDiagnosis: diagnosisId,
            });

            if ("error" in result) {
                setErrorMessages((prev) => ({
                    ...prev,
                    [scanId]: result.error?.message || "Failed to set diagnosis",
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
        <If replacement={<Loading message="Loading scans..." />} condition={!isLoadingScans && pagination}>
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
                        <Th>Assign Diagnosis</Th>
                        <Th>Patient</Th>
                    </tr>
                }
                body={(item: Scan) => (
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
                        <Td>{item.doctorDiagnosis?.name ?? "-"}</Td>
                        <Td>
                            {!item.doctorDiagnosis ? (
                                <div className="flex flex-col gap-2">
                                    <Select
                                        id={`diagnosis-${item.id}`}
                                        value={selectedDiagnoses[item.id] || ""}
                                        onChange={(e) =>
                                            handleDiagnosisChange(String(item.id), e.target.value)
                                        }
                                        options={[
                                            { label: "Select diagnosis", value: "" },
                                            ...diagnosisList.map((d) => ({
                                                label: d.name,
                                                value: String(d.id),
                                            })),
                                        ]}
                                        disabled={isLoadingDiagnoses || isSettingDiagnosis[String(item.id)]}
                                    />
                                    {errorMessages[String(item.id)] && (
                                        <div className="text-red-500 text-sm">{errorMessages[String(item.id)]}</div>
                                    )}
                                </div>
                            ) : (
                                <span className="text-green-600">Diagnosis assigned</span>
                            )}
                        </Td>
                        <Td>{item.patient?.name ?? "-"}</Td>
                    </tr>
                )}
            />
        </If>
    );
}
