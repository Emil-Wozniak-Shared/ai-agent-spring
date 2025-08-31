import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { Button } from "../ui/button";
import { fetchArticleAbstract } from "~/store/slices/pubmedSlice";
import {
  Card,
  CardAction,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../ui/card";
import { createManyDocuments } from "~/store/slices/documentSlice";
import { addNotification } from "~/store/slices/appSlice";

const CurrentPubmedArticle = () => {
  const dispatch = useAppDispatch();
  const { currentArticle, loading } = useAppSelector((state) => state.pubmed);
  function handleFetchAbstract(id: string): void {
    dispatch(fetchArticleAbstract(id));
  }
  if (!currentArticle) {
    return <div id="empty-current-article"></div>;
  }

  const handleCreateDocuments = async () => {
    try {
      const document = {
        text: currentArticle.abstract!!,
        metadata: {
          id: currentArticle.id,
          title: currentArticle.title,
          authors: currentArticle.authors,
          source: currentArticle.source,
          pubDate: currentArticle.pubDate,
        },
      };

      await dispatch(createManyDocuments([document])).unwrap();

      dispatch(
        addNotification({
          message: `1 document created successfully!`,
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
  return (
    <Card className="current-article mb-4">
      <CardHeader>
        <CardTitle>
          <h2 className="text-2xl">Selected Article</h2>
        </CardTitle>
        <CardAction>
          <Button
            disabled={!currentArticle.abstract}
            onClick={() => handleCreateDocuments()}
          >
            {"Store"}
          </Button>
        </CardAction>
      </CardHeader>
      <CardContent className="article-details mb-4">
        <h3>{currentArticle.title}</h3>
        <p>
          <strong>Authors:</strong> {currentArticle.authors}
        </p>
        <p>
          <strong>Journal:</strong> {currentArticle.source}
        </p>
        {currentArticle.abstract && (
          <div className="abstract">
            <h4>Abstract:</h4>
            <p>{currentArticle.abstract}</p>
          </div>
        )}
      </CardContent>
      <CardFooter className="">
        <Button
          onClick={() => handleFetchAbstract(currentArticle.id)}
          disabled={loading}
        >
          Get Abstract
        </Button>
      </CardFooter>
    </Card>
  );
};

export default CurrentPubmedArticle;
