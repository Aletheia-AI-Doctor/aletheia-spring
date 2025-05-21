import {type Scan, useGetScansQuery} from "~/features/scans/scansApiSlice";
import If from "~/components/if";
import Loading from "~/components/Loading";
import React, {useEffect} from "react";
import {Table, Td, Th} from "~/components/Table/table";
import {useSearchParams} from "react-router";

interface ScansTableProps {
    refetchNow?: boolean;
}

export default function ScansTable({refetchNow}: ScansTableProps) {

    const [params, setParams] = useSearchParams();

    const req:any = {
        page: parseInt(params.get('page') ?? '1'),
    }
    if(params.has('patientId')) {
        req.patientId = parseInt(params.get('patientId')!);
    }

    const { data: pagination, isLoading: isLoadingScans, refetch } = useGetScansQuery(req);

    useEffect(() => {
        refetch();
    }, [refetchNow]);

    return (
        <If
            replacement={<Loading message="Loading scans..." />}
            condition={!isLoadingScans && pagination !== undefined}>
            <Table
                pagination={pagination!}
                header={(
                    <tr>
                        <Th first>ID</Th>
                        <Th>Image</Th>
                        <Th>Model</Th>
                        <Th>Model Diagnosis</Th>
                        <Th>Doctor Diagnosis</Th>
                        <Th>Patient</Th>
                    </tr>
                )}

                body={(item: Scan) => (
                    <tr key={item.id}>
                        <Td first>#{item.id}</Td>
                        <Td>
                            <img
                                src={item.imageUrl}
                                alt="Scan"
                                style={{ width: "100px", height: "100px" }} />
                        </Td>
                        <Td>{item.model.name}</Td>
                        <Td>{item.modelDiagnosis.name}</Td>
                        <Td>{item.doctorDiagnosis?.name ?? "-"}</Td>
                        <Td>{item.patient?.name ?? "-"}</Td>
                    </tr>
                )}
            />
        </If>
    );
}