import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { Button } from "../ui/button";
import { fetchArticleAbstract } from "~/store/slices/pubmedSlice";

const CurrentPubmedArticle = () => {
  const dispatch = useAppDispatch();
  const { currentArticle, loading } = useAppSelector((state) => state.pubmed);
  function handleFetchAbstract(id: string): void {
    dispatch(fetchArticleAbstract(id));
  }
  if (!currentArticle) {
    return <div id="empty-current-article"></div>;
  }
  return (
    <div className="current-article mb-4">
      <h2>Selected Article</h2>
      <div className="article-details mb-4">
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
      </div>
      <div className="">
        <Button
          onClick={() => handleFetchAbstract(currentArticle.id)}
          disabled={loading}
        >
          Get Abstract
        </Button>
      </div>
    </div>
  );
};

export default CurrentPubmedArticle;
