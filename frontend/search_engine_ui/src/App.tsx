
import React, {ChangeEvent, Component} from 'react';
import "./App.css";

interface AppState {
    query : string,
    results: any[] | null

}
class App extends Component<{}, AppState> {

  constructor(props: any) {
    super(props);
    this.state = {query : "", results : []};
  }

    // updates the search bar
    updateSearch = (e: ChangeEvent<HTMLInputElement>) => {
        this.setState({ query: e.target.value});
    };


    // search on enter click (same behavior as searchWrapper)
    onKey = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
            this.search().then().catch(() => {alert("Server not up!");
                this.setState({ query: "", results: []});});
        }
    }

    // wrapper the allows to catch errors
    searchWrapper = () => {
        this.search().then().catch(() => {alert("Server not up!");
            this.setState({ query: "", results: []});});
    }

    async search() {
        if (this.state.query === "") {
            // nothing to search
            return;
        }
        let res = await fetch("http://localhost:4567/search/" + this.state.query);
        if (!res.ok) {
            alert("Unable to find path!");
            return;
        }
        let r :any[] = await res.json(); // acts as a cast
        if (r.length === 0) {
            // no need to display empty results, instead give an alert
            alert("Nothing Found!");
        }
         this.setState({
             results: r
         })
    }

  render() {
    return (
        <div>
            <div className="logo">
            <img alt={''} src={"SearchEngineLogo.png"} height={100}/>
            </div>
            <div className="search">

            <input
                className="input"
                onKeyPress={this.onKey}
                placeholder="Search the Web"
                onChange={this.updateSearch}
                value={this.state.query}
            />
            <button className={"go"} onClick={this.searchWrapper} >GO</button>
            </div>
            {this.state.results && this.state.results.map(({link}) => (
                <div className="search_results">
                    <br>{}</br>
                    <a href={link} target="_blank" rel="noreferrer">{link}</a>
                    <br>{}</br>
                </div>


            ))}
        </div>
    );
  }


}

export default App;
