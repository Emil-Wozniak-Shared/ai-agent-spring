import React from 'react'
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  fetchAllUsers,
  createUser,
  updateUser,
  type User,
  emptyUser,
} from "../store/slices/userSlice";
import { updateOrcid } from "../store/slices/orcidSlice";
import { Label } from "@radix-ui/react-label";
import { Input } from "~/components/ui/input";
import { Button } from "~/components/ui/button";

const Profile = () => {
    const dispatch = useAppDispatch();
    const { users, loading, error } = useAppSelector((state) => state.users);
    return (
        <div>
            <OrcidProfile/>
            <section className="mt-2">
            </section>
        </div>
    )
}

const OrcidProfile = () => {
    const dispatch = useAppDispatch();
    const { orcid, loading, error } = useAppSelector((state) => state.orcid);
    const [orcidState, setOrcidState] = React.useState(orcid);
    const sendUpdateOrcid = (e: React.FormEvent) => {
        e.preventDefault();
        dispatch(updateOrcid(orcidState.id));
    }

    return (
        <section id="orcid-section">
            <h4 className="text-lg">{"You can find Orcid ID here"}</h4>
            <form
                onSubmit={sendUpdateOrcid}
                className="shadow-md rounded px-8 pt-6 pb-8 mb-4"
                >
                    <div className="mb-6">
                      <Label htmlFor="orcid-id">Orcid ID:</Label>
                      <Input
                        id="orcid-id"
                        type="text"
                        value={orcidState.id}
                        onChange={(e) => {
                          setOrcidState({ ...orcidState, id: e.target.value })
                       } }
                        required
                        className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline"
                      />
                    </div>
                    <Button type="submit" variant="outline">
                        {loading ? "Updating..." : "Update"}
                    </Button>
            </form>
            <a  target="_blank"
                rel="noopener noreferrer"
                href="https://orcid.org/orcid-search/search?searchQuery="
                className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline">
                {"Click here if you don't remember your ORCID ID"}
            </a>
        </section>
    )
}

export default Profile