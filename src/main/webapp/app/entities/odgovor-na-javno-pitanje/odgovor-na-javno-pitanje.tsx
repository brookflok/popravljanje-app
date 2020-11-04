import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './odgovor-na-javno-pitanje.reducer';
import { IOdgovorNaJavnoPitanje } from 'app/shared/model/odgovor-na-javno-pitanje.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOdgovorNaJavnoPitanjeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const OdgovorNaJavnoPitanje = (props: IOdgovorNaJavnoPitanjeProps) => {
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

  const { odgovorNaJavnoPitanjeList, match, loading } = props;
  return (
    <div>
      <h2 id="odgovor-na-javno-pitanje-heading">
        <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.home.title">Odgovor Na Javno Pitanjes</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.home.createLabel">Create new Odgovor Na Javno Pitanje</Translate>
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
                  placeholder={translate('popravljanjeApp.odgovorNaJavnoPitanje.home.search')}
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
        {odgovorNaJavnoPitanjeList && odgovorNaJavnoPitanjeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.odgovor">Odgovor</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.datum">Datum</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.prikaz">Prikaz</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.javnoPitanje">Javno Pitanje</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.dodatniinfoUser">Dodatniinfo User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {odgovorNaJavnoPitanjeList.map((odgovorNaJavnoPitanje, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${odgovorNaJavnoPitanje.id}`} color="link" size="sm">
                      {odgovorNaJavnoPitanje.id}
                    </Button>
                  </td>
                  <td>{odgovorNaJavnoPitanje.odgovor}</td>
                  <td>
                    {odgovorNaJavnoPitanje.datum ? (
                      <TextFormat type="date" value={odgovorNaJavnoPitanje.datum} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{odgovorNaJavnoPitanje.prikaz ? 'true' : 'false'}</td>
                  <td>
                    {odgovorNaJavnoPitanje.javnoPitanje ? (
                      <Link to={`javno-pitanje/${odgovorNaJavnoPitanje.javnoPitanje.id}`}>{odgovorNaJavnoPitanje.javnoPitanje.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {odgovorNaJavnoPitanje.dodatniinfoUser ? (
                      <Link to={`dodatni-info-user/${odgovorNaJavnoPitanje.dodatniinfoUser.id}`}>
                        {odgovorNaJavnoPitanje.dodatniinfoUser.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${odgovorNaJavnoPitanje.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${odgovorNaJavnoPitanje.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${odgovorNaJavnoPitanje.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.home.notFound">No Odgovor Na Javno Pitanjes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ odgovorNaJavnoPitanje }: IRootState) => ({
  odgovorNaJavnoPitanjeList: odgovorNaJavnoPitanje.entities,
  loading: odgovorNaJavnoPitanje.loading,
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OdgovorNaJavnoPitanje);
