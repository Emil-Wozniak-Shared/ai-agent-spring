import React from "react";
import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { addNotification } from "~/store/slices/appSlice";
import { createManyDocuments } from "~/store/slices/documentSlice";
import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";

const CreateDocumentForm = () => {
  const dispatch = useAppDispatch();
  const { documents, searchResults, loading, error } = useAppSelector(
    (state) => state.documents,
  );
  const [newDocs, setNewDocs] = React.useState([{ text: "", metadata: {} }]);

  const handleCreateDocuments = async (e: React.FormEvent) => {
    e.preventDefault();
    const validDocs = newDocs.filter((doc) => doc.text);
    if (validDocs.length === 0) return;

    try {
      await dispatch(createManyDocuments(validDocs)).unwrap();
      setNewDocs([{ text: "", metadata: {} }]);
      dispatch(
        addNotification({
          message: `${validDocs.length} document(s) created successfully!`,
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to create documents",
          type: "error",
        }),
      );
    }
  };

  const addDocumentField = () => {
    setNewDocs([...newDocs, { text: "", metadata: {} }]);
  };

  // const updateDocument = (
  //   index: number,
  //   field: "title" | "content",
  //   value: string,
  // ) => {
  //   const updated = [...newDocs];
  //   updated[index][field] = value;
  //   setNewDocs(updated);
  // };

  return (
    <Card className="documents-section">
      <CardHeader>
        <CardTitle>
          <h2 className="text-2xl">Create Documents</h2>
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleCreateDocuments}>
          {/* {newDocs.map((doc, index) => (
            <div key={index} className="document-input">
              <input
                type="text"
                placeholder="Document title"
                value={doc.title}
                onChange={(e) => updateDocument(index, "title", e.target.value)}
              />
              <textarea
                placeholder="Document content"
                value={doc.content}
                onChange={(e) =>
                  updateDocument(index, "content", e.target.value)
                }
                rows={3}
              />
            </div>
          ))} */}
          <div className="form-actions">
            <button type="button" onClick={addDocumentField}>
              Add Another Document
            </button>
            <button type="submit" disabled={loading}>
              {loading ? "Creating..." : "Create Documents"}
            </button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
};

export default CreateDocumentForm;
