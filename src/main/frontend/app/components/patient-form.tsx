import React, {type FormEvent, useState} from "react";
import {type Patient, useAddPatientMutation} from "~/features/patient/patientApiSlice";
import Input from "~/components/input";
import Select from "~/components/select";
import Button from "~/components/button";

interface PatientFormProps {
    onClose?: () => void;
    onSuccess?: (patient: Patient) => void;
}

export default function PatientForm({onClose, onSuccess} : PatientFormProps) {

    const [addPatient, { isLoading }] = useAddPatientMutation();
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const [newPatient, setNewPatient] = useState({
        name: '',
        sex: '',
        status: 'pending'
    });

    const handleAddPatient = async (event: FormEvent) => {
        event.preventDefault();

        const response = await addPatient(newPatient);

        if (response.error) {
            // @ts-ignore
            setErrorMessage(response.error.message);
            return;
        }

        onSuccess && onSuccess(response.data);

        setNewPatient({
            name: '',
            sex: '',
            status: 'pending'
        });

        onClose && onClose();
    };

    return (
        <div>
            <h2 className="text-lg font-semibold mb-4">Add New Patient</h2>
            <form onSubmit={handleAddPatient}>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <Input
                        required
                        value={newPatient.name}
                        onChange={(e) => setNewPatient({...newPatient, name: e.target.value})}
                        id="name"
                        label="Name"
                    />

                    <Select
                        id="sex"
                        onChange={(e) => setNewPatient({...newPatient, sex: e.target.value})}
                        placeholder="-- Choose --"
                        label="Sex"
                        required
                        value={newPatient.sex}
                        options={[
                            {label: "Male", value: "male"},
                            {label: "Female", value: "female"},
                        ]}
                    />
                </div>

                {errorMessage && (
                    <div className="mt-4 text-red-600">
                        {errorMessage}
                    </div>
                )}
                <div className="flex justify-end space-x-3 mt-6">
                    <Button
                        type="submit"
                        color="heavy-green"
                        disabled={isLoading}
                    >
                        {isLoading ? "Loading..." : "Save Patient"}
                    </Button>
                </div>
            </form>
        </div>
    );
}