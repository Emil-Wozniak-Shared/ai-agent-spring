import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  fetchAllUsers,
  createUser,
  updateUser,
  type User,
  emptyUser,
} from "../store/slices/userSlice";

const Profile = () => {
    const dispatch = useAppDispatch();
    const { users, loading, error } = useAppSelector((state) => state.users);
    return (
        <div>
             {users && users.length > 1 && (
                          <div>
                            {users.map((user) =>  (
                                <UserCard
                                  key={user.id}
                                  user={user}
                                  isEditing={editingUser === user.id}
                                  onEdit={() => setEditingUser(user.id!!)}
                                  onSave={(userData) => handleUpdateUser(user.id!!, userData)}
                                  onCancel={() => setEditingUser(null)}
                                />
                              ))
                            }
                          </div>
            )}
            <a
             target="_blank"
             rel="noopener noreferrer"
             href="https://orcid.org/orcid-search/search?searchQuery=">
             Search ORCID
           </a>
        </div>
    )
}

export default Profile