import React, {useState} from 'react';
import {type Patient, useGetPatientsQuery} from "~/features/patient/patientApiSlice";
import {Link, useSearchParams} from 'react-router';
import PatientForm from "~/components/patient-form";
import Loading from "~/components/Loading";
import Modal from "~/components/modal";
import Button from "~/components/button";
import Card from "~/components/Card";
import {Table, Td, Th} from "~/components/Table/table";
import type {Scan} from "~/features/scans/scansApiSlice";
import If from "~/components/if";
import Title from "~/components/title";

export function meta() {
    return [
        {title: "Patient Management"},
        {name: "description", content: "Manage patient records"},
    ];
}

export default function PatientPage() {
    const [params, setParams] = useSearchParams();

    const {
        data: pagination,
        isLoading,
    } = useGetPatientsQuery({
        page: parseInt(params.get('page') ?? '1'),
    });

    const [showAddForm, setShowAddForm] = useState(false);

    return (
        <div>
            <Title>Patients</Title>

            <div className="flex justify-between items-center my-6">
                <Button
                    width="w-auto"
                    onClick={() => setShowAddForm(true)}
                >
                    Add patient
                </Button>
            </div>
            

            <Modal open={showAddForm} onClose={() => setShowAddForm(false)}>
                <PatientForm onClose={() => setShowAddForm(false)}/>
            </Modal>

            <Card>
                <If
                    replacement={<Loading message="Loading scans..."/>}
                    condition={!isLoading && pagination !== undefined}>
                    <Table
                        pagination={pagination!}
                        header={(
                            <tr>
                                <Th first>ID</Th>
                                <Th>Name</Th>
                                <Th>Admission Date</Th>
                                <Th>State</Th>
                                <Th>Add Scan</Th>
                                <Th>Actions</Th>
                            </tr>
                        )}

                        body={(item: Patient) => (
                            <tr key={item.id}>
                                <Td first>#{item.id}</Td>
                                <Td>{item.name}</Td>
                                <Td>{item.admissionDate?.toString()}</Td>
                                <Td>
                                                          <span
                                                              className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                                                  item.status === 'pending' ? 'bg-yellow-100 text-yellow-800' :
                                                                      item.status === 'discharged' ? 'bg-green-100 text-green-800' :
                                                                          'bg-gray-100 text-gray-800'
                                                              }`}>
                                            {item.status}
                                        </span>
                                </Td>
                                <Td>
                                    <Link
                                        to={`/diagnose?patientId=${item.id}`}
                                        className="text-blue-600 hover:text-blue-900"
                                    >
                                        Add Scan
                                    </Link>
                                </Td>
                                <Td>
                                    <Link
                                        to={`/patients/${item.id}`}
                                        className="text-blue-600 hover:text-blue-900 mr-4"
                                    >
                                        View
                                    </Link>
                                </Td>
                            </tr>
                        )}
                    />
                </If>
            </Card>
        </div>
    );
}