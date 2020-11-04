import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { openFile, byteSize, Translate, translate, ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './slika.reducer';
import { ISlika } from 'app/shared/model/slika.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISlikaProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Slika = (props: ISlikaProps) => {
  const [search, setSearch] = useState('');

  useEffect(() => {
    props.getEntities();
  }, []);

  const startSearching = () => {
    if (search) {
      props.getSearchEntities(search);
    }
  };

  const clear = () => {
    setSearch('');
    props.getEntities();
  };

  const handleSearch = event => setSearch(event.target.value);

  const { slikaList, match, loading } = props;
  return (
    <div>
      <h2 id="slika-heading">
        <Translate contentKey="popravljanjeApp.slika.home.title">Slikas</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="popravljanjeApp.slika.home.createLabel">Create new Slika</Translate>
        </Link>
      </h2>
      <Row>
        <Col sm="12">
          <AvForm onSubmit={startSearching}>
            <AvGroup>
              <InputGroup>
                <AvInput
                  type="text"
                  name="search"
                  value={search}
                  onChange={handleSearch}
                  placeholder={translate('popravljanjeApp.slika.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </AvGroup>
          </AvForm>
        </Col>
      </Row>
      <div className="table-responsive">
        {slikaList && slikaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.slika.ime">Ime</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.slika.slika">Slika</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.slika.uploaded">Uploaded</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.slika.mainslika">Mainslika</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.slika.mainslika">Mainslika</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.slika.galerija">Galerija</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {slikaList.map((slika, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${slika.id}`} color="link" size="sm">
                      {slika.id}
                    </Button>
                  </td>
                  <td>{slika.ime}</td>
                  <td>
                    {slika.slika ? (
                      <div>
                        {slika.slikaContentType ? (
                          <a onClick={openFile(slika.slikaContentType, slika.slika)}>
                            <img src={`data:${slika.slikaContentType};base64,${slika.slika}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {slika.slikaContentType}, {byteSize(slika.slika)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{slika.uploaded ? <TextFormat type="date" value={slika.uploaded} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{slika.mainslika ? <Link to={`main-slika/${slika.mainslika.id}`}>{slika.mainslika.ime}</Link> : ''}</td>
                  <td>{slika.mainslika ? <Link to={`profilna-slika/${slika.mainslika.id}`}>{slika.mainslika.ime}</Link> : ''}</td>
                  <td>{slika.galerija ? <Link to={`galerija/${slika.galerija.id}`}>{slika.galerija.ime}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${slika.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${slika.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${slika.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="popravljanjeApp.slika.home.notFound">No Slikas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ slika }: IRootState) => ({
  slikaList: slika.entities,
  loading: slika.loading,
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Slika);
