import classes from "./SearchBar.module.css";
import SearchIcon from "@mui/icons-material/Search";
import ClearIcon from "@mui/icons-material/Cancel";

const SearchBar: React.FC<{
  value: string;
  onClear: () => void;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
  placeholder?: string;
  onSearch: () => void;
  customStyle?: string;
}> = ({
  value,
  onClear,
  onChange,
  onSearch,
  placeholder = "",
  customStyle = "",
}) => {
  return (
    <div
      className={`${classes.searchContainer} ${
        customStyle ? classes[customStyle] : ""
      }`}
    >
      {value && (
        <ClearIcon className={classes.clearIcon} onClick={() => onClear()} />
      )}
      <input
        id="search"
        name="search"
        className={classes.searchInput}
        type="text"
        value={value}
        placeholder={placeholder}
        autoComplete="off"
        onChange={onChange}
        onClick={() => onSearch()}
      />
      <SearchIcon
        className={`${classes.searchIcon} ${
          value.length > 2
            ? classes.activeSearchIcon
            : classes.disabledSearchIcon
        }`}
        onClick={() => onSearch()}
      />
    </div>
  );
};

export default SearchBar;
